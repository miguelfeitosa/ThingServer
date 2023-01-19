package eixo.games.thingserver.configuration;

import eixo.games.thingserver.persistence.ThingSaverFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  Factory Class to Allow Spring Boot Dependency Injection
 *  DISCUSSION POINTS:
 *    - what is and why dependency injection - testing, configuration
 *    - Spring, Angular, other frameworks
 */
@Configuration
@Slf4j //In Log we trust, thanks Lombok for removing the boilerplate
public class BeanFactory {
    @Bean
    ThingSaverFacade getThingSaverFacade() {
        log.debug(String.format("Creating %s Bean",ThingSaverFacade.class));
        return ThingSaverFacade.getSingleton();
    }
}