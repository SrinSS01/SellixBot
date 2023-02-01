package io.github.srinss01.sellixbot.commands;

import io.github.srinss01.sellixbot.sellix_api.Sellix;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import io.github.srinss01.sellixbot.utils.Embeds;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Stock extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;

    public Stock(Sellix sellix) {
        super("stock", "Retrieve products stock.");
        this.sellix = sellix;
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();
        Map<String, Object> allProducts = sellix.getAllProducts();
        if (allProducts == null) {
            hook.editOriginal("An error occurred.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(allProducts.get("status")));
        if (status != 200) {
            hook.editOriginalEmbeds(Embeds.Error(String.valueOf(allProducts.get("error")))).queue();
            return;
        }
        var data = (Map<String, List<Map<String, Object>>>) allProducts.get("data");
        if (data == null) {
            hook.editOriginal("An error occurred.").queue();
            return;
        }
        var products = data.get("products");
        if (products == null) {
            hook.editOriginal("An error occurred.").queue();
            return;
        }
        if (products.isEmpty()) {
            hook.editOriginal("No products found.").queue();
            return;
        }
        hook.editOriginalEmbeds(Embeds.Stock(products)).queue();
    }
}
