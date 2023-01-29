package io.github.srinss01.temproleaddbot;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootApplication
public class TempRoleAddBotApplication {
    public static final Logger LOGGER = LoggerFactory.getLogger(TempRoleAddBotApplication.class);

    public static void main(String[] args) throws IOException {
        val config = new File("config");
        if (!config.exists()) {
            val mkdir = config.mkdir();
            if (!mkdir) {
                LOGGER.error("Failed to create config directory");
                return;
            }
            LOGGER.info("Created config directory");
        }
        val properties = new File("config/application.yml");
        if (!properties.exists()) {
            try (
                    val is = TempRoleAddBotApplication.class.getResourceAsStream("../../../../application.yml")
            ) {
                if (is == null) {
                    return;
                }
                byte[] bytes = is.readAllBytes();
                String str = new String(bytes);
                Files.writeString(properties.toPath(), str.substring(0, str.indexOf("#internals")));
                LOGGER.info("Created application.yml file");
            }
            return;
        }
        SpringApplication.run(TempRoleAddBotApplication.class, args);
    }

}
