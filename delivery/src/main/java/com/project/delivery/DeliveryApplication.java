package com.project.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;

@SpringBootApplication
public class DeliveryApplication {

    private InetAddress address;

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }


    // TODO: Connections w/ front end
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {

//            private InetAddress serverAddress;

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");

//                registry.addMapping("/**").allowedOrigins(@Value("${server.address}" String serverAddress){
//                    this.serverAddress = getAddress();
//                }
            }
        };
    }

}
