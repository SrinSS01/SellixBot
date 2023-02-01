package io.github.srinss01.sellixbot.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Objects;

public class Say extends CommandDataImpl implements ICustomCommandData {
    public Say() {
        super("say", "Make the bot say something.");
        addOption(OptionType.STRING, "message", "The message you want the bot to say.", true);
    }
    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.replyFormat("```\n%s\n```", Objects.requireNonNull(interaction.getOption("message")).getAsString()).queue();
    }
}
