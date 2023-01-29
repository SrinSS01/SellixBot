package io.github.srinss01.temproleaddbot.commands;

import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class Stop extends CommandDataImpl implements ICustomCommandData {
    public Stop() {
        super("stop", "Stops the bot");
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.reply("Stopping bot...").setEphemeral(true).queue();
        interaction.getJDA().shutdown();
    }
}
