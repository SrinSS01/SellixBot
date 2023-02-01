package io.github.srinss01.sellixbot.commands;

import io.github.srinss01.sellixbot.sellix_api.Sellix;
import io.github.srinss01.sellixbot.utils.Embeds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class Coupon extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    public Coupon(Sellix sellix) {
        super("create_coupon", "Create a coupon code.");
        this.sellix = sellix;
        addOption(OptionType.STRING, "code", "Code of the Coupon.", true);
        // discount_value
        addOption(OptionType.INTEGER, "discount_value", "Discount value of the Coupon.", true);
        // max_uses
        addOption(OptionType.INTEGER, "max_uses", "How many times can the coupon be used, defaulted to -1.", false);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        String code = Objects.requireNonNull(interaction.getOption("code")).getAsString();
        int discount_value = Objects.requireNonNull(interaction.getOption("discount_value")).getAsInt();
        var max_uses = interaction.getOption("max_uses");
        Sellix.Coupon coupon = Sellix.Coupon.of(code, discount_value, max_uses == null ? -1 : max_uses.getAsInt());
        Map<String, Object> response = sellix.createCoupon(coupon);
        if (response == null) {
            interaction.getHook().editOriginal("Coupon not created.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(response.get("status")));
        if (status != 200) {
            interaction.getHook().editOriginalEmbeds(Embeds.Error(String.valueOf(response.get("error")))).queue();
            return;
        }
        Map<String, String> data = (Map<String, String>) response.get("data");
        if (data == null) {
            interaction.getHook().editOriginal("Coupon not created.").queue();
            return;
        }
        interaction.getHook().editOriginalEmbeds(Embeds.Success(Map.of("uniqid", data.get("uniqid"), "message", String.valueOf(response.get("message"))))).queue();
    }
}