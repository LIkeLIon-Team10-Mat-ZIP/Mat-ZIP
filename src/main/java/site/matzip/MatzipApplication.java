package site.matzip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MatzipApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatzipApplication.class, args);
    }

}
