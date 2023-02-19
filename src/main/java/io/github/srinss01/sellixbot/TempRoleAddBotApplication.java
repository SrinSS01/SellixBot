package io.github.srinss01.sellixbot;

import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.srinss01.sellixbot.auth.ActivationStatus;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@SpringBootApplication
public class TempRoleAddBotApplication extends JFrame {
    public static final Logger LOGGER = LoggerFactory.getLogger(TempRoleAddBotApplication.class);
    private static final Scanner scanner = new Scanner(System.in);

    static boolean headless = GraphicsEnvironment.isHeadless();
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        ActivationStatus.init();
        // if environment variables are not set, get them from user input
        val config = new File("config");
        if (!config.exists()) {
            val mkdir = config.mkdir();
            if (!mkdir) {
                LOGGER.error("Failed to create config directory");
                System.exit(1);
            }
            LOGGER.info("Created config directory");
        }
        val properties = new File("config/application.yml");
        if (!properties.exists()) {
            // get environment variables
            var activationKey = Objects.requireNonNullElse(validateEnv("ACTIVATION_KEY"), ask("Enter activation key: ", "Activation Key"));
            try {
                if (!ActivationStatus.check(activationKey)) {
                    if (!headless) {
                        JOptionPane.showMessageDialog(null, "Invalid activation key", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    LOGGER.error("Invalid activation key");
                    System.exit(1);
                }
            } catch (UnirestException e) {
                if (!headless) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                LOGGER.error("Error while checking activation key", e);
                System.exit(1);
            }
            var token = Objects.requireNonNullElse(validateEnv("TOKEN"), ask("Enter bot token: ", "Bot Token"));
            var temporaryRoleIdsStr = Objects.requireNonNullElse(validateEnv("TEMPORARY_ROLE_IDS"), ask("Enter temporary role IDs (separated by commas): ", "Temporary Role IDs"));
            var logChannelID = Objects.requireNonNullElse(validateEnv("LOG_CHANNEL_ID"), ask("Enter log channel ID: ", "Log Channel ID"));
            var sellixAuth = Objects.requireNonNullElse(validateEnv("SELLIX_AUTH"), ask("Enter Sellix auth: ", "Sellix Auth"));
            var roleToGive = Objects.requireNonNullElse(validateEnv("ROLE_TO_GIVE"), ask("Enter role to give: ", "Role To Give"));
            var timePeriodInSeconds = Long.parseLong(Objects.requireNonNullElse(validateEnv("TIME_PERIOD_IN_SECONDS"), ask("Enter time period in seconds: ", "Time Period In Seconds")));
            scanner.close();
            String[] temporaryRoleIds = temporaryRoleIdsStr.split("\\s*,\\s*");
            Config _config = new Config();
            _config.setToken(token);
            _config.setTemporaryRoleIds(List.of(temporaryRoleIds));
            _config.setKey(activationKey);
            _config.setLogChannelId(logChannelID);
            _config.setSellixAuth(sellixAuth);
            _config.setRoleToGive(roleToGive);
            _config.setTimePeriodInSeconds(timePeriodInSeconds);
            try {
                Files.writeString(properties.toPath(), _config.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Created application.yml file, please restart the bot");
        }
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TempRoleAddBotApplication.class).headless(GraphicsEnvironment.isHeadless()).run(args);
    }

    private static String validateEnv(String keyName) {
        LOGGER.info("searching for env({})...", keyName);
        var keyVal = System.getenv(keyName);
        if (keyVal == null) {
            LOGGER.info("{} not found, skipping...", keyName);
        } else {
            LOGGER.info("{} found, continuing...", keyName);
            return keyVal;
        }
        return null;
    }

    private static String ask(String message, String title) {
        if (headless) {
            System.out.print(message);
            return scanner.nextLine();
        } else {
            return JOptionPane.showInputDialog(
                    null,
                    message,
                    title,
                    JOptionPane.PLAIN_MESSAGE
            );
        }
    }
}
