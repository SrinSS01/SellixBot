package io.github.srinss01.temproleaddbot.sellix_api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import okhttp3.*;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(staticName = "login")
public class Sellix {
    private final String apiKey;
    private static final String url = "https://dev.sellix.io/v1/";
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Sellix.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Map<String, Object> getAllOrders() {
        return apiRequest("orders");
    }

    public Map<String, Object> getOrder(String id) {
        return apiRequest("orders/" + id);
    }
    // replace order
    public Map<String, Object> replaceOrder(String id, int quantity, String product_id) {
        RequestBody body = RequestBody.create(GSON.toJson(Map.of("quantity", quantity, "product_id", product_id)), MediaType.parse("application/json"));
        return apiRequest("orders/replacement/" + id, body, "POST");
    }
    // all products
    public Map<String, Object> getAllProducts() {
        return apiRequest("products");
    }

    // product
    public Map<String, Object> getProduct(String id) {
        return apiRequest("products/" + id);
    }

    // create product
    public Map<String, Object> createProduct(Product product) {
        RequestBody body = RequestBody.create(GSON.toJson(product), MediaType.parse("application/json"));
        return apiRequest("products", body, "POST");
    }

    // edit product
    public Map<String, Object> editProduct(String id, Product product) {
        RequestBody body = RequestBody.create(GSON.toJson(product), MediaType.parse("application/json"));
        return apiRequest("products/" + id, body, "PUT");
    }

    // delete product
    public Map<String, Object> deleteProduct(String id) {
        return apiRequest("products/" + id, null, "DELETE");
    }

    // feedbacks
    public Map<String, Object> getAllFeedbacks() {
        return apiRequest("feedbacks");
    }

    // feedback
    public Map<String, Object> getFeedback(String id) {
        return apiRequest("feedbacks/" + id);
    }

    // reply to feedback
    public Map<String, Object> replyToFeedback(String id, String message) {
        RequestBody body = RequestBody.create(GSON.toJson(Map.of("reply", message)), MediaType.parse("application/json"));
        return apiRequest("feedbacks/reply/" + id, body, "POST");
    }

    // coupon endpoints
    public Map<String, Object> getAllCoupons() {
        return apiRequest("coupons");
    }

    public Map<String, Object> getCoupon(String id) {
        return apiRequest("coupons/" + id);
    }

    public Map<String, Object> createCoupon(Coupon coupon) {
        RequestBody body = RequestBody.create(GSON.toJson(coupon), MediaType.parse("application/json"));
        return apiRequest("coupons", body, "POST");
    }

    // edit coupon
    public Map<String, Object> editCoupon(
            String id,
            Map<String, Object> fields
    ) {
        RequestBody body = RequestBody.create(GSON.toJson(fields), MediaType.parse("application/json"));
        return apiRequest("coupons/" + id, body, "PUT");
    }

    // delete coupon
    public Map<String, Object> deleteCoupon(String id) {
        return apiRequest("coupons/" + id, null, "DELETE");
    }

    // query endpoints
    public Map<String, Object> getAllQueries() {
        return apiRequest("queries");
    }

    public Map<String, Object> getQuery(String id) {
        return apiRequest("queries/" + id);
    }
    // get query at a specific page
    public Map<String, Object> getQuery(int page) {
        return apiRequest("queries?page=" + page);
    }

    public Map<String, Object> replyToQuery(String id, String message) {
        RequestBody body = RequestBody.create(GSON.toJson(Map.of("reply", message)), MediaType.parse("application/json"));
        return apiRequest("queries/reply/" + id, body, "POST");
    }

    // close query
    public Map<String, Object> closeQuery(String id) {
        return apiRequest("queries/close/" + id, null, "POST");
    }

    // reopen query
    public Map<String, Object> reopenQuery(String id) {
        return apiRequest("queries/reopen/" + id, null, "POST");
    }

    // blacklist endpoints
    public Map<String, Object> getAllBlacklists() {
        return apiRequest("blacklists");
    }

    public Map<String, Object> getBlacklist(String id) {
        return apiRequest("blacklists/" + id);
    }

    public Map<String, Object> createBlacklist(Blacklist blacklist) {
        RequestBody body = RequestBody.create(GSON.toJson(blacklist), MediaType.parse("application/json"));
        return apiRequest("blacklists", body, "POST");
    }

    // update blacklist
    public Map<String, Object> updateBlacklist(String id, Blacklist blacklist) {
        RequestBody body = RequestBody.create(GSON.toJson(blacklist), MediaType.parse("application/json"));
        return apiRequest("blacklists/" + id, body, "PUT");
    }

    // delete blacklist
    public Map<String, Object> deleteBlacklist(String id) {
        return apiRequest("blacklists/" + id, null, "DELETE");
    }

    public Map<String, Object> apiRequest(String params, RequestBody body, String method) {
        Request request = new Request.Builder()
                .url(url + params)
                .method(method, body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                logger.error("Error while making request to Sellix API: Response body is null");
                return null;
            }
            if (response.code() != 200) {
                logger.error("Error while making request to Sellix API: {}", responseBody.string());
                return null;
            }
            //noinspection unchecked
            return GSON.fromJson(responseBody.string(), Map.class);
        } catch (Exception e) {
            logger.error("Error while making request to Sellix API", e);
            return null;
        }
    }
    public Map<String, Object> apiRequest(String params) {
        return apiRequest(params, null, "GET");
    }

    @AllArgsConstructor(staticName = "of")
    @Getter @Setter
    @ToString
    private static class Product{
            String title;
            String description;
            int price;
            List<String> gateways;
            Type type;

            public enum Type {
                SERIALS, FILE, SERVICE, DYNAMIC, INFO_CARD, SUBSCRIPTION;

                @Override
                public String toString() {
                    return name();
                }
            }
    }
    @AllArgsConstructor(staticName = "of")
    @Getter @Setter
    @ToString
    public static class Coupon {
        String code;
        int discount_value;
        int max_uses;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter @Setter
    @ToString
    public static class Blacklist {
        Type type;
        String data;
        String note;

        public enum Type {
            EMAIL, IP, COUNTRY, ISP, ASN, HOST;

            @Override
            public String toString() {
                return name();
            }
        }
    }

    public enum QueryStatus {
        PENDING, CLOSED, SHOP_REPLY, CUSTOMER_REPLY, OPENED
    }
}
