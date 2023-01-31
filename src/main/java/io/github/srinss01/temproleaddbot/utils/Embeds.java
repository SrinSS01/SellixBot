package io.github.srinss01.temproleaddbot.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Embeds {
    private static final int color = 0x2f3136;
    // success color
    private static final int success = 0x43b581;
    // error color
    private static final int error = 0xf04747;

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
                .addField(productType, productType.equals("SERIALS") ? format(String.join(", ", (List<String>) order.get("serials")).trim()) : "", false)
                .setColor(color)
                .build();
    }

    /*public static MessageEmbed Product(Object data) {
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
    }*/

    public static MessageEmbed Error(String errorMsg) {
        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(format(errorMsg))
                .setColor(error)
                .build();
    }

    private static String format(Object str) {
        return "`%s`".formatted(str);
    }

//    @SuppressWarnings("SameParameterValue")
    /*private static String formatter(String format, Object... args) {
        return String.format(format, args);
    }*/

    public static MessageEmbed Success(Map<String, String> objects) {
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(success);
        objects.forEach((key, value) -> builder.addField(key, format(value), false));
        return builder.build();
    }

    public static MessageEmbed Stock(List<Map<String, Object>> allProducts) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Stock")
                .setColor(color);
        allProducts.forEach(product -> {
            int stock = (int) Double.parseDouble(product.get("stock").toString());
            String title = (stock != 0? "\\✅ ": "\\❌ ") + product.get("title");
            builder.addField(title, stock == -1? "`Service`": format(stock), true);
        });
        return builder.build();
    }
}
