package io.github.srinss01.sellixbot;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

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
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter bot token: ");
            String token = scanner.nextLine();
            System.out.print("Enter temporary role IDs (separated by commas): ");
            String[] temporaryRoleIds = scanner.nextLine().split("\\s*,\\s*");
//            System.out.println("Enter admin role IDs (separated by commas): ");
//            String[] adminRoleIds = scanner.nextLine().split("\\s*,\\s*");
            System.out.print("Enter log channel ID: ");
            String logChannelID = scanner.nextLine();
            System.out.print("Enter Sellix auth: ");
            String sellixAuth = scanner.nextLine();
            System.out.print("Enter role to give: ");
            String roleToGive = scanner.nextLine();
            System.out.print("Enter time period in seconds: ");
            long timePeriodInSeconds = scanner.nextLong();
            scanner.close();
            Config _config = new Config();
            _config.setToken(token);
            _config.setTemporaryRoleIds(List.of(temporaryRoleIds));
//            _config.setAdminRoleIds(List.of(adminRoleIds));
            _config.setLogChannelId(logChannelID);
            _config.setSellixAuth(sellixAuth);
            _config.setRoleToGive(roleToGive);
            _config.setTimePeriodInSeconds(timePeriodInSeconds);
            Files.writeString(properties.toPath(), _config.toString());
            LOGGER.info("Created application.yml file, please restart the bot");
            return;
        }
        SpringApplication.run(TempRoleAddBotApplication.class, args);
    }
}
