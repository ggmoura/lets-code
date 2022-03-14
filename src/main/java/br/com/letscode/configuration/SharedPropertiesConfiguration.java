package br.com.letscode.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SharedPropertiesConfiguration {


    @Value("${client.omdbapi.apikey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
