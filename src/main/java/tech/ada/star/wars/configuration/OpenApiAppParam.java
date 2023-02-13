package tech.ada.star.wars.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OpenApiAppParam {

	@Value("${letscode.app.project.description:Let's Code - Aprenda a programar}")
	private String description;

	@Value("${letscode.app.project.name:Let's Code Api}")
	private String title;

	@Value("${letscode.app.project.appversion:0.0.1-SNAPSHOT}")
	private String appVersion;

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public String getAppVersion() {
		return appVersion;
	}

}