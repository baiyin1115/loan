package springboot.reload.plugin.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springboot.reload.plugin.endpoint.OnlineReloadEndpoint;

@Configuration
public class OnlineReloadConfiguration implements BeanFactoryAware, EnvironmentAware{
	
	private ConfigurableListableBeanFactory beanFactory;
	
	private Environment environment;
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
		System.out.println(this.environment.getActiveProfiles());
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		 this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}
	
	@Bean
	public OnlineReloadEndpoint onlineReloadEndpoint() {
		return new OnlineReloadEndpoint();
	}
	
}
