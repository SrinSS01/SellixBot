package io.github.srinss01.sellixbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Objects;

public class Embed extends CommandDataImpl implements ICustomCommandData {
    public Embed() {
        super("embed", "Make the bot say something in an embed.");
        addOption(OptionType.STRING, "message", "The message you want the bot to say.", true);
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.replyEmbeds(
                new EmbedBuilder()
                        .setDescription(String.format("```\n%s\n```", Objects.requireNonNull(interaction.getOption("message")).getAsString()))
                        .setColor(generateRandomColor())
                        .setFooter("Requested by " + interaction.getUser().getAsTag(), interaction.getUser().getAvatarUrl())
                        .setTimestamp(interaction.getTimeCreated())
                        .build())
                .queue();
    }

    // private static generate random color int
    private static int generateRandomColor() {
        return (int) (Math.random() * 0x1000000);
    }
}
