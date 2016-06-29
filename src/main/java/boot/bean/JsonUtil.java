package boot.bean;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import flexjson.JSONSerializer;

@Component
public class JsonUtil {

	public String stringify(Object obj) {
		JSONSerializer serializer = new JSONSerializer();
		return serializer.exclude("*.class").serialize(obj);
	}

	public <T> T parse(String json, Class<T> clazz) {
		// JSONDeserializer<T> deserializer = new JSONDeserializer<T>();
		// return deserializer.deserialize(json);
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			return null;
		}
	}
}
