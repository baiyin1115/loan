package springboot.reload.plugin.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import springboot.reload.plugin.annotation.EnableOnlineReload;

public class OnlineReloadSelector implements ImportSelector, EnvironmentAware{

	private Environment environment;
	
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Class<?> annotationType = EnableOnlineReload.class;
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(annotationType.getName(), false));
		String[] activeProfile = attributes.getStringArray("activeProfile");
		if (activeProfile == null || activeProfile.length == 0) {
			throw new UnsupportedOperationException("activeProfile must be set effective values");
		}
		Set<String> envProfiles = new HashSet<String>(Arrays.asList(environment.getActiveProfiles()));
		Set<String> reloadProfiles = new HashSet<String>(Arrays.asList(activeProfile));
		if (!envProfiles.equals(reloadProfiles)) {
			throw new UnsupportedOperationException("environment profile error");
		}
		return new String[] {OnlineReloadConfiguration.class.getName()};
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
