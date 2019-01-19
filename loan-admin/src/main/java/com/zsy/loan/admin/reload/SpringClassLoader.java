package com.zsy.loan.admin.reload;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringClassLoader extends AnnotationConfigApplicationContext implements springboot.reload.plugin.core.SpringBootClassLoader{

	@Override
	public ClassLoader getMyClassLoader() {
		return super.getClassLoader();
	}

}
