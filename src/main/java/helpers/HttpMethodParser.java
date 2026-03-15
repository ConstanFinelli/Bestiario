package helpers;

import java.io.IOException;

import jakarta.servlet.ServletException;

public class HttpMethodParser {
	public interface MethodCallback {
		default void put() throws ServletException, IOException {};
		default void delete() throws ServletException, IOException {};
		default void patch() throws ServletException, IOException {};
	};
	
	public boolean Redirect(String flag, MethodCallback callback) throws ServletException, IOException {
		if (flag == null) return false;
		
		switch (flag.toUpperCase()) {
        case "PUT":
            callback.put();
            return true;
        case "DELETE":
            callback.delete();
            return true;
        case "PATCH":
            callback.patch();
            return true;
        default:
            return false;
    }
	}
}
