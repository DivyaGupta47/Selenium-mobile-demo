package apis;

import java.util.List;
import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.Config;
/**
 * API class for retrieving and working with stage-related data of an order.
 *
 * This class provides methods to:
 * - Fetch the list of all stages for a given order.
 * - Get the raw list of stage data as a map.
 *
 * Endpoint used: /api/v1/stage/order/list
 */

public class StageAPI {
	
	 public static Response getStages(Integer orderId) {
	        return RestAssured
	            .given()
	                .baseUri(Config.BASE_URI)
	                //.header("Cookie", Config.getCookieHeader())
	                .header("Authorization", "Bearer " + Config.getSessionToken())
	                .queryParam("orderId", orderId)
	                .queryParam("isAssignee", false)
	                .queryParam("sortKey", "sequence")
	                .queryParam("sortValue", "ASC")
	            .when()
	                .get("/api/v1/stage/order/list");
	    }

	 
	 public static List<Map<String, Object>> getStageList(Integer orderId) {
	        Response response = getStages(orderId);
	        return response.jsonPath().getList("items");
	    }
}
