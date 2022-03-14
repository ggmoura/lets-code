package br.com.letscode.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CombinadoOpenApiAppParam {

	@Value("${letscode.app.project.description:Combinado - Information Technology}")
	private String description;

	@Value("${letscode.app.project.name:Combinado Api}")
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