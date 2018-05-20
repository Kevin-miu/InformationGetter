package infomation;

import com.google.gson.Gson;

public class JsonUtil {

	public static String transformToJson(Object object) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(object);

		return jsonStr;
	}
}
