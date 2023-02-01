package io.github.srinss01.sellixbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;

public class UserLogFormatter {
    public static MessageEmbed format(User user, String channel, String commandId, String commandName) {
        return new EmbedBuilder()
                .setTitle("Command Executed: </%s:%s>".formatted(commandName, commandId))
                .addField("User", Embeds.format(user.getAsTag()), true)
                .addField("ID", Embeds.format(user.getId()), true)
                .addField("Channel", channel, false)
                .setFooter("Logged", user.getAvatarUrl())
                .setTimestamp(OffsetDateTime.now())
                .setColor(Embeds.randomColor())
                .build();
    }
}
