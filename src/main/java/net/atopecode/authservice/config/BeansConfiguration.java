package net.atopecode.authservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfiguration {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}