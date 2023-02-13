package tech.ada.star.wars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import static tech.ada.star.wars.Constant.BASE_PACKAGE;

@EnableAutoConfiguration
@ComponentScan(basePackages = { BASE_PACKAGE })
public class AdaTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdaTechApplication.class, args);
    }

}
