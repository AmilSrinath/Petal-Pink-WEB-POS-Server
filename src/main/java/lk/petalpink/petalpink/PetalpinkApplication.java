package lk.petalpink.petalpink;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PetalpinkApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PetalpinkApplication.class);
        app.addInitializers(new ConfigLoader());
        app.run(args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        long maxSize = 50L * 1024 * 1024; // 50MB in bytes
        long maxRequest = 100L * 1024 * 1024; // 100MB in bytes
        return new MultipartConfigElement("", maxSize, maxRequest, 0);
    }
}