package kit;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
