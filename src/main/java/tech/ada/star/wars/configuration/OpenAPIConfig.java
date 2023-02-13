package tech.ada.star.wars.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static tech.ada.star.wars.Constant.OpenApiParam.ADA_URL;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI(OpenApiAppParam params) {
		return new OpenAPI()
			.info(new Info()
				.title(params.getDescription())
				.contact(contact())
				.version(params.getAppVersion())
				.description(params.getDescription())
				.termsOfService(ADA_URL)
				.license(new License().name("Ada Tech - 1.0").url(ADA_URL))
			);
	}

	private Contact contact() {
		return new Contact().name("Ada Tech").url(ADA_URL).email("contact@ada.tech");
	}

}