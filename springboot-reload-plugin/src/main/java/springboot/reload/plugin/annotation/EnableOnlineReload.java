package springboot.reload.plugin.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import springboot.reload.plugin.configuration.OnlineReloadSelector;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(OnlineReloadSelector.class)
public @interface EnableOnlineReload {

	String[] activeProfile() default {"dev", "qa"};
	
	String[] enableReloadPackages() default {};
	
	boolean needAuth() default false;
	
}