package io.github.rafaeljc.designpatterns;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Notification API", version = "v1", description = "Factory Method + Builder demo"))
public class DesignpatternsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignpatternsApplication.class, args);
	}

}
