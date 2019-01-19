package springboot.reload.plugin.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.springframework.stereotype.Component;

@Component
public class ClassReloader extends ClassLoader{

	private static ThreadLocal<Tools> currentReloadJarFile = new ThreadLocal<Tools>();

	private static final Set<String> excludedPackages = new HashSet<String>(
			Arrays.asList("java.", "javax.", "sun.", "oracle."));
	
	public void reload(File jarFile, String name, ClassLoader runtimeClassLoader) throws ClassNotFoundException, ZipException, IOException {
		currentReloadJarFile.set(new Tools(jarFile, name, runtimeClassLoader));
		try (ZipFile jar = new ZipFile(jarFile);){
			Enumeration<? extends ZipEntry> jarEntry = jar.entries();
			ZipEntry entry = null;
			while (jarEntry.hasMoreElements()) {
				entry = jarEntry.nextElement();
				String classFullFileName = entry.getName().replace("/", ".");
				if(!classFullFileName.endsWith("class")){
					continue;
				}
				loadClass(classFullFileName, true);
			}	
		} finally {
			if (jarFile.exists()) {
				jarFile.delete();
			}
			currentReloadJarFile.set(null);
		}
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Tools tools = currentReloadJarFile.get();
		if (name.equals(tools.getReloadName()) && !isExclude(name)) {
			String sourceJar = tools.getCurrentReloadJarFile().getAbsolutePath();
			Class<?> targetClass = null;
			try {
				byte[] classByteArray = loadBytesFromJar(name, sourceJar);
				name = name.substring(0, name.lastIndexOf("."));
				targetClass = defineClass(name , classByteArray, 0, classByteArray.length);
				return targetClass;
			} catch (IOException e) {
				
			}
		}
		return tools.getRuntimeClassLoader().loadClass(name);
	}
	
	@SuppressWarnings("resource")
	private byte[] loadBytesFromJar(String name, String sourceJar) throws IOException {
		ZipFile jarFile = new ZipFile(sourceJar);
		Enumeration<? extends ZipEntry> jarEntry = jarFile.entries();
		ZipEntry entry = null;
		boolean existsClass = false;
		if(!name.endsWith(".class")){
			name+=".class";
		}
		while (jarEntry.hasMoreElements()) {
			entry = jarEntry.nextElement();
			if (existsClass = (entry.getName().replace("/", ".").equals((name)))) {
				break;
			}
		}
		if (!existsClass) {
			return null;
		}
		BufferedInputStream bufferedInputStream = new BufferedInputStream(jarFile.getInputStream(entry));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bread;
		for (; ((bread = bufferedInputStream.read()) != -1); ) {
			baos.write(bread);
		}
		byte[] readBytes = baos.toByteArray();
		baos.close();
		bufferedInputStream.close();
		jarFile.close();
		if (readBytes == null) {
			throw new IOException("read "+sourceJar+" file"+name+" error");
		}
		return readBytes;
	}
	
	private static boolean isExclude(String name) {
		for(String prefix : excludedPackages) {
			if (name.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public class Tools {
		private File currentReloadJarFile;
		private String reloadName;
		private ClassLoader runtimeClassLoader;

		public Tools() {
		}

		public Tools(File currentReloadJarFile, String reloadName, ClassLoader runtimeClassLoader) {
			super();
			this.currentReloadJarFile = currentReloadJarFile;
			this.reloadName = reloadName;
			this.runtimeClassLoader = runtimeClassLoader;
		}

		public File getCurrentReloadJarFile() {
			return currentReloadJarFile;
		}

		public void setCurrentReloadJarFile(File currentReloadJarFile) {
			this.currentReloadJarFile = currentReloadJarFile;
		}

		public String getReloadName() {
			return reloadName;
		}

		public void setReloadName(String reloadName) {
			this.reloadName = reloadName;
		}

		public ClassLoader getRuntimeClassLoader() {
			return runtimeClassLoader;
		}

		public void setRuntimeClassLoader(ClassLoader runtimeClassLoader) {
			this.runtimeClassLoader = runtimeClassLoader;
		}
	}

}