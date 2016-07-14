package kit;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Component
public class JSON {

	public static String stringify(Object obj) {
		// JSONSerializer serializer = new JSONSerializer();
		// return serializer.exclude("*.class").serialize(obj);
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new Hibernate5Module());
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
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
