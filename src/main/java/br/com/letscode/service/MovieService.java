package br.com.letscode.service;

import br.com.letscode.client.MovieClient;
import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.configuration.SharedPropertiesConfiguration;
import br.com.letscode.controller.model.movie.MovieDTO;
import br.com.letscode.entity.Movie;
import br.com.letscode.exception.BusinessException;
import br.com.letscode.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieClient client;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SharedPropertiesConfiguration properties;

    public MovieDTO createMovieByTytle(String title) {
        MovieDTO movie = client.getMovieByTitle(properties.getApiKey(), title);
        validateMovie(movie);
        movieRepository.save(mapper.map(movie, Movie.class));
        return movie;
    }

    public MovieDTO createMovieByImdbID(String imdbID) {
        MovieDTO movie = client.getMovieByImdbID(properties.getApiKey(), imdbID);
        validateMovie(movie);
        movieRepository.save(mapper.map(movie, Movie.class));
        return movie;
    }

    private void validateMovie(MovieDTO movie) {
        if ("False".equals(movie.getResponse())) {
            throw new BusinessException(ResponseMessage.error(movie.getError()));
        }
        if ("N/A".equals(movie.getImdbRating()) || "N/A".equals(movie.getImdbVotes())) {
            throw new BusinessException(ResponseMessage.error(
                    "Filme sem dados suficientes na plataforma {0}, ImdbRating={1}, ImdbVotes={2}",
                    "http://www.omdbapi.com", movie.getImdbRating(), movie.getImdbVotes()));
        }
    }

}
