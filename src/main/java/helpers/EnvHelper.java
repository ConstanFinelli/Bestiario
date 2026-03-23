package helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvHelper {
	public static Dotenv dotEnv = Dotenv.load();
	
	public static String get(String variableName) {
		return dotEnv.get(variableName);
		}
}
