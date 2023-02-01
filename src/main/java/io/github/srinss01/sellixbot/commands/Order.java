package io.github.srinss01.sellixbot.commands;

import io.github.srinss01.sellixbot.sellix_api.Sellix;
import io.github.srinss01.sellixbot.utils.Embeds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Map;
import java.util.Objects;

public class Order extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    public Order(Sellix sellix) {
        super("order", "Retrieve order information (excluding delivered goods).");
        this.sellix = sellix;
        addOption(OptionType.STRING, "id", "The ID of the order you want to look up.", true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }
    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();
        String id = Objects.requireNonNull(interaction.getOption("id")).getAsString();
        Map<String, Object> order = sellix.getOrder(id);
        if (order == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(order.get("status")));
        if (status != 200) {
            interaction.getHook().editOriginalEmbeds(Embeds.Error(String.valueOf(order.get("error")))).queue();
            return;
        }
        Object data = order.get("data");
        if (data == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        hook.editOriginalEmbeds(Embeds.Order(data)).queue();
    }
}
