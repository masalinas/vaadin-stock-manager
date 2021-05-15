package io.oferto.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Appconfig {
	@Bean
    public AppContext singletonBean() {
        return new AppContext();
    }
}
