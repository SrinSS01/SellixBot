package io.github.srinss01.sellixbot;

import com.mashape.unirest.http.exceptions.UnirestException;
import io.github.srinss01.sellixbot.auth.ActivationStatus;
import io.github.srinss01.sellixbot.database.Database;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;

import java.awt.*;

import static io.github.srinss01.sellixbot.TempRoleAddBotApplication.headless;
import static net.dv8tion.jda.api.requests.GatewayIntent.*;

@Component
@AllArgsConstructor
public class Main implements CommandLineRunner {
    final Database database;
    final Events events;
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Main.class);
    @Override
    public void run(String... args) throws UnirestException {
        Config config = database.getConfig();
        String activationKey = config.getKey();
        if (!Config.isActivated && !ActivationStatus.check(activationKey)) {
            if (!headless) {
                JOptionPane.showMessageDialog(null, "Invalid activation key", "Error", JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error("Invalid activation key");
            System.exit(1);
        }
        String token = config.getToken();
        if (token == null || token.isEmpty()) {
            return;
        }
        LOGGER.info("Starting bot with token: {}", token);
        try {
            JDABuilder
                    .createDefault(token)
                    .enableIntents(
                            GUILD_MEMBERS,
                            MESSAGE_CONTENT,
                            GUILD_EMOJIS_AND_STICKERS,
                            GUILD_VOICE_STATES,
                            GUILD_PRESENCES
                    )
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .addEventListeners(events)
                    .disableCache(
                            CacheFlag.EMOJI,
                            CacheFlag.STICKER,
                            CacheFlag.VOICE_STATE
                    ).build();
        } catch (InvalidTokenException e) {
            if (GraphicsEnvironment.isHeadless()) {
                throw e;
            } else JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + token, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
