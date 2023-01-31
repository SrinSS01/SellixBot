package io.github.srinss01.temproleaddbot.commands;

import io.github.srinss01.temproleaddbot.sellix_api.Sellix;
import io.github.srinss01.temproleaddbot.utils.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class Serials extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    public Serials(Sellix sellix) {
        super("serials", "Retrieve delivered goods (excluding order info).");
        this.sellix = sellix;
        addOption(OptionType.STRING, "order_id", "The order ID.", true);
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();
        Map<String, Object> order = sellix.getOrder(Objects.requireNonNull(interaction.getOption("order_id")).getAsString());
        if (order == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(order.get("status")));
        if (status != 200) {
            hook.editOriginalEmbeds(Embeds.Error(String.valueOf(order.get("error")))).queue();
            return;
        }
        Map<String, Map<String, Object>> data = (Map<String, Map<String, Object>>) order.get("data");
        if (data == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        Map<String, Object> orderData = data.get("order");
        if (orderData == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        List<String> serials = (List<String>) orderData.get("serials");
        if (serials == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        var product_title = orderData.get("product_title");
        var quantity = (int) Double.parseDouble(orderData.get("quantity").toString());
        hook.editOriginalEmbeds(new EmbedBuilder()
                        .setTitle("Serials")
                        .setDescription(String.format("```\n%s\n```", String.join("\n", serials)))
                        .setFooter(String.format("✅ Product: %s | Amount: %s", product_title, quantity))
                .build()).queue();
    }
}