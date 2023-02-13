package tech.ada.star.wars.configuration;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
public class AdaTechConfiguration {

    @Bean
    @Primary
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint injectionPoint) {
        Class<?> declaringClass = null;
        if (injectionPoint.getField() != null) {
            declaringClass = injectionPoint.getField().getDeclaringClass();
        } else if (injectionPoint.getMethodParameter() != null) {
            declaringClass = injectionPoint.getMethodParameter().getDeclaringClass();
        } else {
            declaringClass = injectionPoint.getClass();
        }
        return LoggerFactory.getLogger(declaringClass);
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setDestinationNameTokenizer((name, nameableType) -> new String[] { name });
        mapper.getConfiguration().setSourceNameTokenizer((name, nameableType) -> new String[] { name });
        return mapper;
    }

}
