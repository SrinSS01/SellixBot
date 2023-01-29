package io.github.srinss01.temproleaddbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Embeds {
    private static final int color = 0x00ff3c;

    public static MessageEmbed Order(Object data) {
        Map<String, Object> order = ((Map<String, Map<String, Object>>) data).get("order");
        String productType = order.get("product_type").toString();
        long createdAt = (long) Double.parseDouble(order.get("created_at").toString());
        return new EmbedBuilder()
                .setTitle("Order Data")
                .setDescription("order: " + format(order.get("uniqid")))
                .addField("Product", format(order.get("product_title").toString()), true)
                .addField("Price", format(order.get("total").toString() + " USD"), true)
                .addField("Status", format(order.get("status").toString()), true)
                .addField("Quantity", format(order.get("quantity").toString()), true)
                .addField("Email", format(order.get("customer_email").toString()), true)
                .addField("Gateway", format(order.get("gateway").toString()), true)
                .addField("Date", format(new SimpleDateFormat("d MMMM yyyy HH:mm").format(new Date(createdAt * 1000))), true)
                .addField("Type", format(productType), true)
                .addField(productType, productType.equals("SERIALS") ? format(String.join(", ", (List<String>) order.get("serials")).trim()) : order.get("file_attachment_uniqid").toString(), false)
                .setColor(color)
                .build();
    }

    public static MessageEmbed Product(Object data) {
        Map<String, Object> product = ((Map<String, Map<String, Object>>) data).get("product");
        return new EmbedBuilder()
                .setTitle("Product Data")
                .setDescription("product: " + format(product.get("uniqid")))
                .addField("Title", format(product.get("title").toString()), true)
                .addField("Price", format(product.get("price").toString()), true)
                .addField("\u200b", "\u200b", true)
                .addField("Type", format(product.get("type").toString()), true)
                .addField("Stock", format(product.get("sold_count").toString()), true)
                .addField("\u200b", "\u200b", true)
                .addField("Sold", format((int) Double.parseDouble(product.get("sold_count").toString())), true)
                .addField("Average Score", formatter("`%.1f`", Double.parseDouble(product.get("average_score").toString())), true)
                .addField("\u200b", "\u200b", true)
                .setColor(color)
                .build();
    }

    public static MessageEmbed Query(Object data) {
        Map<String, Object> query = ((Map<String, Map<String, Object>>) data).get("query");
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Query Data")
                .setDescription("query: " + format(query.get("uniqid")))
                .addField("Title", format(query.get("title").toString()), true)
                .addField("Customer Email", format(query.get("customer_email").toString()), true)
                .setColor(color);
        List<Map<String, Object>> messages = (List<Map<String, Object>>) query.get("messages");
        messages.forEach(message -> {
            var role = message.get("role");
            var msg = message.get("message");
            var createdAt = message.get("created_at");
            builder.addField(
                    "%s %s".formatted(role, new SimpleDateFormat("d MMMM yyyy HH:mm").format(new Date((long) Double.parseDouble(createdAt.toString()) * 1000))),
                    format(msg.toString()),
                    false
            );
        });
        return builder.build();
    }

    private static String format(Object str) {
        return "`%s`".formatted(str);
    }

    @SuppressWarnings("SameParameterValue")
    private static String formatter(String format, Object... args) {
        return String.format(format, args);
    }
}
