package io.github.srinss01.sellixbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

public class Shop extends CommandDataImpl implements ICustomCommandData {
    public Shop() {
        super("shop", "Get the shop link.");
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.replyEmbeds(new EmbedBuilder()
                .setDescription(
                        """
                        **[Explore RagingWarzone's digital store](https://ragingwarzone.mysellix.io/)**

                        View RagingWarzone's digital store: get the digital goods you need instantly with our many payment gateways in just a few clicks, or get in touch with us should you need help.
                        """)
                .build()).addActionRow(Button.link("https://ragingwarzone.mysellix.io/", "Click here")).queue();
    }
}