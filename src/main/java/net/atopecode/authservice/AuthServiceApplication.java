package net.atopecode.authservice;

//import java.util.Arrays;

import net.atopecode.authservice.datadb.DataDBInitializerComponent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	/**
	 * Este método se ejecuta cuando arranca el Servicio Web.
	 * @param ctx
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx, DataDBInitializerComponent dBInitComponent) {
		return args -> {
			logSpringBeans(ctx);
			initDBData(dBInitComponent);
		};
	}

	//Se muestran por consola los Spring Beans que se han creado.
	private void logSpringBeans(ApplicationContext ctx) {
		/*System.out.println("\r\nLet's inspect the beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}*/
	}

	//Carga de datos iniciales en B.D.:
	private void initDBData(DataDBInitializerComponent dBInitComponent){
		dBInitComponent.config();
	}
	
}
