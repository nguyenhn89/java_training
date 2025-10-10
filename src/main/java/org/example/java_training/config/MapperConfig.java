package org.example.java_training.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        // ignore null attributes on the source object on copying
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }
}
