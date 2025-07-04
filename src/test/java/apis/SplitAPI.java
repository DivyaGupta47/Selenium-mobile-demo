package apis;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for handling the split operation on orders.
 *
 * This class provides a method to:
 * - Mark an order as not split using a boolean flag.
 *
 * Endpoint used: /api/v1/orders/split
 */
public class SplitAPI {
	//Split order-NO
    public static Response splitOrder(int orderId, boolean isSplitted) {
        return RestAssured.given()
                .baseUri(Config.BASE_URI)
                .header("Authorization", "Bearer " + Config.getSessionToken())
                .contentType("application/json")
                .queryParam("orderId", orderId)
                .queryParam("isSplitted", isSplitted)
                .when()
                .post("/api/v1/orders/split");
    }
}
