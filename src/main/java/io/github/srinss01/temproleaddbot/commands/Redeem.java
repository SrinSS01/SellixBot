package io.github.srinss01.temproleaddbot.commands;

import io.github.srinss01.temproleaddbot.Config;
import io.github.srinss01.temproleaddbot.database.Database;
import io.github.srinss01.temproleaddbot.database.UserOrders;
import io.github.srinss01.temproleaddbot.sellix_api.Sellix;
import io.github.srinss01.temproleaddbot.utils.Embeds;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class Redeem extends CommandDataImpl implements ICustomCommandData {
    private final Sellix sellix;
    private final Database database;
    public Redeem(Database database, Sellix sellix) {
        super("redeem", "If order is completed it will give the user the role(s)");
        this.sellix = sellix;
        this.database = database;
        addOption(OptionType.STRING, "order_id", "ID of the completed order", true);
    }
    @Override
    public void execute(SlashCommandInteraction interaction) {
        interaction.deferReply().queue();
        InteractionHook hook = interaction.getHook();
        Member member = interaction.getMember();
        Guild guild = interaction.getGuild();
        if (member == null || guild == null) {
            interaction.getHook().editOriginal("Something went wrong.").queue();
            return;
        }

        String id = Objects.requireNonNull(interaction.getOption("order_id")).getAsString();
        Map<String, Object> order = sellix.getOrder(id);
        if (order == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        int status = (int) Double.parseDouble(String.valueOf(order.get("status")));
        if (status != 200) {
            hook.editOriginalEmbeds(Embeds.Error(String.valueOf(order.get("error")))).queue();
            return;
        }
        Object data = order.get("data");
        Map<String, Object> orderData = ((Map<String, Map<String, Object>>) data).get("order");
        if (orderData == null) {
            hook.editOriginal("Order not found.").queue();
            return;
        }
        String orderStatus = (String) orderData.get("status");
        if (!orderStatus.equals("COMPLETED")) {
            hook.editOriginal("Order is not completed.").queue();
            return;
        }
        Config config = database.getConfig();
        Role roleById = guild.getRoleById(config.getRoleToGive());
        if (roleById == null) {
            hook.editOriginal("Role not found.").queue();
            return;
        }
        if (database.getUserOrdersRepository().findById(id).isEmpty()) {
            database.getUserOrdersRepository().save(new UserOrders(id, member.getIdLong()));
            guild.addRoleToMember(member, roleById).queue();
            for (String temporaryRoleId : config.getTemporaryRoleIds()) {
                Role role = guild.getRoleById(temporaryRoleId);
                if (role != null) {
                    guild.addRoleToMember(member, role).queue(
                        v -> guild.removeRoleFromMember(member, role).queueAfter(config.getTimePeriodInSeconds(), TimeUnit.SECONDS)
                    );
                }
            }
            hook.editOriginal("Roles added.").queue();
        } else {
            hook.editOriginal("Order already redeemed.").queue();
        }
    }
}
