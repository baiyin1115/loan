package springboot.reload.plugin.endpoint;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import springboot.reload.plugin.core.ClassReloader;
import springboot.reload.plugin.core.SpringBootClassLoader;
import springboot.reload.plugin.model.OnlineReloadProperties;
import springboot.reload.plugin.model.ReloadResponse;
import springboot.reload.plugin.support.ReloadHistoryService;
import springboot.reload.plugin.support.ReloadHistoryService.ReloadHistory;

@ControllerEndpoint(id = "online-reload")
public class OnlineReloadEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(OnlineReloadEndpoint.class);
	
//	@Autowired
	private OnlineReloadProperties onlineReloadProperties = new OnlineReloadProperties();

	@Autowired
	private ClassReloader classReloader;

	@Autowired
	private SpringBootClassLoader springClassLoader;
	
	@Autowired
	private ReloadHistoryService reloadHistoryService;
	
	@RequestMapping(value = "debug-reload-spring", method = RequestMethod.POST)
	public ResponseEntity<ReloadResponse<String>> uploadSpring(HttpServletRequest request) {
		String saveTempFileDirectory = onlineReloadProperties.getTemppath();
		String user = getIpAddress(request);
		String className = request.getParameter("reloadClassName");
		if (StringUtils.isBlank(user) || StringUtils.isBlank(className)) {
			return ResponseEntity.ok(ReloadResponse.newFailure("参数错误", "缺少必须要信息,请明示你的IP和你要替换的类"));
		}
		if (!className.endsWith(".class")) {
			className += ".class";
		}
		String uploadTempFilePath = buildTempFilename(saveTempFileDirectory, user);
		boolean saveTemp = ajaxUpload(request, uploadTempFilePath);
		if (!saveTemp) {
			return ResponseEntity.ok(
					ReloadResponse.newFailure(-1, String.format("当前系统环境硬盘可能满了或当前用户在%s下没有操作权限", saveTempFileDirectory)));
		}
		return doReload(user, className, uploadTempFilePath);
	}
	
	@GetMapping(value = "debug-last")
	public ResponseEntity<ReloadHistory> reloadHistory(@RequestParam(value = "className", required = true) String className) {
		return ResponseEntity.ok(reloadHistoryService.last(className));
	}
	
	
	private ResponseEntity<ReloadResponse<String>> doReload(String user, String className, String uploadTempFilePath) {
		try {
			classReloader.reload(new File(uploadTempFilePath), className, springClassLoader.getMyClassLoader());
		} catch (Exception e) {
			logger.error("reload error", e);
			if (e instanceof java.io.FileNotFoundException) {
				// ignore
				logger.warn("不要在意这个细节,当然在意你也可以研究一下,下次分享");
			} else {
				return ResponseEntity.ok(ReloadResponse.newFailure(-1, String.format("请查看日志", e.getMessage())));
			}
		} catch (Error e) {
			if (e.getMessage().contains("attempted  duplicate class definition for name")) {
				logger.warn("不要在意这个细节,当然在意你也可以研究一下,下次分享");
			} else {
				return ResponseEntity.ok(ReloadResponse.newFailure(-1, String.format("请查看日志", e.getMessage())));
			}
		}
		reloadHistoryService.addReloadHistory(user, className);
		return ResponseEntity.ok(ReloadResponse.newSuccess("热加载成功,你可以继续操作了"));
	}
	
	private String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String buildTempFilename(String tempDir, String user) {
		return onlineReloadProperties.getTemppath() + File.separator + user + "_" + System.currentTimeMillis() + ".jar";
	}
	
	private boolean ajaxUpload(HttpServletRequest request, String uploadTempFilePath) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMaps = multiRequest.getFileMap();
		if (fileMaps != null && fileMaps.size() == 1) {
			for (Entry<String, MultipartFile> fileEntry : fileMaps.entrySet()) {
				if (fileEntry != null && !fileEntry.getValue().isEmpty()) {
					MultipartFile multiFile = fileEntry.getValue();
					String fileFullPath = uploadTempFilePath;
					File uploadTempFile = new File(fileFullPath);
					try {
						// 临时文件持久化到硬盘
						multiFile.transferTo(uploadTempFile);
					} catch (IllegalStateException | IOException e) {
						logger.error("上传文件持久化到服务器失败,计划持久化文件 ", e);
						return false;
					}
				}
			}
		}
		return true;
	}

	
}