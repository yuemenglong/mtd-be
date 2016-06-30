package boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import boot.bean.ForeignKeyCleaner;

@SpringBootApplication
public class App {
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
		context.getBean(ForeignKeyCleaner.class).clean();
	}
}
