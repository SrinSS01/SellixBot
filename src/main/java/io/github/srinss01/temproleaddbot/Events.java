package io.github.srinss01.temproleaddbot;

import io.github.srinss01.temproleaddbot.commands.*;
import io.github.srinss01.temproleaddbot.database.Database;
import io.github.srinss01.temproleaddbot.sellix_api.Sellix;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Events extends ListenerAdapter {
    final Database database;
    private final CommandsCollection commandsCollection = new CommandsCollection();

    public Events(Database database) {
        this.database = database;
        Sellix sellix = Sellix.login(database.getConfig().getSellixAuth());
        put(new Stop());
        put(new Order(sellix));
        put(new Redeem(database, sellix));
    }
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Events.class);

    private void put(ICustomCommandData commandData) {
        commandsCollection.put(commandData.getName(), commandData);
    }
    @Override
    public void onReady(ReadyEvent event) {
        logger.info("{} is ready.", event.getJDA().getSelfUser().getName());
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event
                .getGuild()
                .updateCommands()
                .addCommands(commandsCollection.values())
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        commandsCollection.get(event.getName()).execute(event);
    }
}
