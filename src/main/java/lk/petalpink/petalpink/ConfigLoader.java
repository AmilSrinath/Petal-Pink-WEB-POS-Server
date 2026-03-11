package lk.petalpink.petalpink;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class ConfigLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        Path configPath = Paths.get("config.txt");

        if (!Files.exists(configPath)) {
            System.err.println("[ConfigLoader] config.txt not found at: "
                    + configPath.toAbsolutePath());
            return;
        }

        Properties props = new Properties();
        try (InputStream is = Files.newInputStream(configPath)) {
            props.load(is);
            MutablePropertySources sources = environment.getPropertySources();
            sources.addFirst(new PropertiesPropertySource("config.txt", props));
            System.out.println("[ConfigLoader] Loaded config.txt from: "
                    + configPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.txt", e);
        }
    }
}