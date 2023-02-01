package io.github.srinss01.sellixbot.commands;

import io.github.srinss01.sellixbot.sellix_api.Sellix;
import io.github.srinss01.sellixbot.utils.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unchecked", "DuplicatedCode"})
public class Replace extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    public Replace(Sellix sellix) {
        super("replace", "Send a replacement in the discord channel, then removes replacement serials from stock.");
        this.sellix = sellix;
        addOption(OptionType.STRING, "order_id", "The order ID of the order you want to replace.", true);
        addOption(OptionType.INTEGER, "amount", "The amount of serials you want to replace.", true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();
        String order_id = Objects.requireNonNull(interaction.getOption("order_id")).getAsString();
        int amount = Objects.requireNonNull(interaction.getOption("amount")).getAsInt();
        Map<String, Object> orderObject = sellix.getOrder(order_id);
        if (orderObject == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(orderObject.get("status")));
        if (status != 200) {
            hook.editOriginalEmbeds(Embeds.Error(String.valueOf(orderObject.get("error")))).queue();
            return;
        }
        Map<String, Map<String, Object>> order_data = (Map<String, Map<String, Object>>) orderObject.get("data");
        if (order_data == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        Map<String, Object> order = order_data.get("order");
        if (order == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        String product_id = (String) order.get("product_id");
        String product_title = (String) order.get("product_title");
        int quantity = (int) Double.parseDouble(String.valueOf(order.get("quantity")));
        Map<String, Object> response = sellix.replaceOrder(order_id, amount, product_id);
        Map<String, Map<String, Object>> data = (Map<String, Map<String, Object>>) sellix.getOrder(order_id).get("data");
        List<String> serials = (List<String>) data.get("order").get("serials");
        if (response == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        status = (int) Double.parseDouble(String.valueOf(response.get("status")));
        if (status != 200) {
            hook.editOriginalEmbeds(Embeds.Error(String.valueOf(response.get("error")))).queue();
            return;
        }
        String message = (String) response.get("message");
        hook.editOriginalEmbeds(new EmbedBuilder()
                        .setDescription(String.format("```\n%s\n```", message))
                        .addField("Serial", String.format("```\n%s\n```", serials.get(serials.size() - 1)).trim(), false)
                        .setFooter(String.format("✅ Product: %s | Amount: %s", product_title, quantity))
                .build()).queue();
    }
}
