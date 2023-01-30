package io.github.srinss01.temproleaddbot.commands;

import io.github.srinss01.temproleaddbot.sellix_api.Sellix;
import io.github.srinss01.temproleaddbot.utils.Embeds;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Map;
import java.util.Objects;

public class DeleteCoupon extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    public DeleteCoupon(Sellix sellix) {
        super("delete_coupon", "Delete a coupon code.");
        this.sellix = sellix;
        addOption(OptionType.STRING, "id", "The unique ID of the coupon you want to delete.", true);
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        String id = Objects.requireNonNull(interaction.getOption("id")).getAsString();
        Map<String, Object> response = sellix.deleteCoupon(id);
        if (response == null) {
            interaction.getHook().editOriginal("Coupon not found.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(response.get("status")));
        if (status != 200) {
            interaction.getHook().editOriginalEmbeds(Embeds.Error(String.valueOf(response.get("error")))).queue();
            return;
        }
        String message = (String) response.get("message");
        if (message == null) {
            interaction.getHook().editOriginal("Coupon not found.").queue();
            return;
        }
        interaction.getHook().editOriginalEmbeds(Embeds.Success(Map.of("message", message))).queue();
    }
}
