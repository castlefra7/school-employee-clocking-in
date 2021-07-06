/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.human_resources;

import mg.human_resources.service.StorageProperties;
import mg.human_resources.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author lacha
 */
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CompagnieAerienne {

    public static void main(String[] args) {
        SpringApplication.run(CompagnieAerienne.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            //storageService.deleteAll();
            storageService.init();
        };
    }
}
