package lk.petalpink.petalpink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetalpinkApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PetalpinkApplication.class);
        app.addInitializers(new ConfigLoader()); // ← register here
        app.run(args);
    }
}