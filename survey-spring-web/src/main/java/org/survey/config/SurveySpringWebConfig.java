package org.survey.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ServiceConfig.class,
        SecurityConfig.class,
        WebMvcConfig.class
})
public class SurveySpringWebConfig {
}
