package io.github.srinss01.temproleaddbot;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Main implements CommandLineRunner {
    final Config config;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    @Override
    public void run(String... args) {
        logger.info("Config: {}", config);
    }
}
