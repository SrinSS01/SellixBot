package io.github.srinss01.sellixbot.commands;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ICustomCommandData extends CommandData {
    void execute(SlashCommandInteraction interaction);
}
