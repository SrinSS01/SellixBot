package io.github.srinss01.sellixbot;

import io.github.srinss01.sellixbot.commands.*;
import io.github.srinss01.sellixbot.database.Database;
import io.github.srinss01.sellixbot.sellix_api.Sellix;
import io.github.srinss01.sellixbot.utils.UserLogFormatter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Events extends ListenerAdapter {
    final Database database;
    private final CommandsCollection commandsCollection = new CommandsCollection();

    public Events(Database database) {
        this.database = database;
        Sellix sellix = Sellix.login(database.getConfig().getSellixAuth());
        put(new Stop());
        put(new Shop());
        put(new Say());
        put(new Embed());
        put(new Order(sellix));
        put(new Redeem(database, sellix));
        put(new Coupon(sellix));
        put(new DeleteCoupon(sellix));
        put(new Stock(sellix));
        put(new Replace(sellix));
        put(new Serials(sellix));
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
        Config config = database.getConfig();
        User user = event.getUser();
        String name = event.getName();
        Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getTextChannelById(config.getLogChannelId()))
                .sendMessageEmbeds(UserLogFormatter.format(user, event.getChannel().getAsMention(), event.getCommandId(), name))
                .queue();
        commandsCollection.get(name).execute(event);
    }
}
