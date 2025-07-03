package utils;
/**
 * Configuration utility class for storing and accessing global constants and session-related data.
 * 
 * This class includes:
 * - Base URI for API endpoints.
 * - Static tokens for CSRF and ORY session (used for authentication headers).
 * - Thread-local session token storage for managing parallel execution safely.
 * 
 * Key Features:
 * - `BASE_URI`: The root URL used by all API endpoints.
 * - `CSRF_TOKEN` & `ORY_SESSION`: Used for building cookie headers when needed.
 * - `sessionToken` (ThreadLocal): Provides thread-safe session token storage, useful in parallel test execution.
 */
public class Config {

	// Base URL for API endpoints
	public static final String BASE_URI = "https://freetrial-mf.kestrelpro.ai";

	    //private static String sessionToken;
	    private static final ThreadLocal<String> sessionToken = new ThreadLocal<>();

	    public static void setSessionToken(String token) {
	        //sessionToken = token;
	    	sessionToken.set(token);
	    }

	    public static String getSessionToken() {
	        //return sessionToken;
	    	 return sessionToken.get();
	    }
}
