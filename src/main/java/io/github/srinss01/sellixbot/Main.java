package io.github.srinss01.sellixbot;

import io.github.srinss01.sellixbot.database.Database;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

@Component
@AllArgsConstructor
public class Main implements CommandLineRunner {
    final Database database;
    final Events events;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);
    @Override
    public void run(String... args) {
        String token = database.getConfig().getToken();
        if (token == null || token.isEmpty()) {
            return;
        }
        logger.info("Starting bot with token: {}", token);
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
    }
}
