package kit;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import flexjson.JSONSerializer;

@Component
public class JSON {

	public static String stringify(Object obj) {
		JSONSerializer serializer = new JSONSerializer();
		return serializer.exclude("*.class").serialize(obj);
	}

	public static <T> T parse(String json, Class<T> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			return null;
		}
	}
}
