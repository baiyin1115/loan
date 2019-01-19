package springboot.reload.plugin.support;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class ReloadHistoryService {
	
	private Map<String , ReloadHistory> classMapping = new HashMap<>();
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void addReloadHistory(String user, String className) {
		classMapping.put(className, new ReloadHistory(simpleDateFormat.format(new Date()), user, className));
	}
	
	public ReloadHistory last(String className){
		ReloadHistory value = classMapping.get(className+".class");
		return value == null ? new ReloadHistory(simpleDateFormat.format(new Date()), "qa_git", className) : value;
	}
	
	public class ReloadHistory{
		private String timestamp;
		private String ip;
		private String className;
		
		public ReloadHistory() {}
		public ReloadHistory(String timestamp, String ip, String className) {
			super();
			this.timestamp = timestamp;
			this.ip = ip;
			this.className = className;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		
	}
	
}