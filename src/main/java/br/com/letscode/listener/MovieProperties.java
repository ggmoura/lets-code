package br.com.letscode.listener;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("letscode.moviebattle")
@PropertySource("classpath:movie.properties")
public class MovieProperties {

	private List<String> movies;

	public List<String> getMovies() {
		return movies;
	}

	public void setMovies(List<String> movies) {
		this.movies = movies;
	}

}
