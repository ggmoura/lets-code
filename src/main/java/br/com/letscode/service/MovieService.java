package br.com.letscode.service;

import br.com.letscode.client.MovieClient;
import br.com.letscode.commons.ResponseMessage;
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

    @Value("${client.omdbapi.apikey}")
    private String apiKey;

    public MovieDTO createMovieByTytle(String title) {
        MovieDTO movie = client.getMovieByTitle(apiKey, title);
        validateMovie(movie);
        movieRepository.save(mapper.map(movie, Movie.class));
        return movie;
    }

    public MovieDTO createMovieByImdbID(String imdbID) {
        MovieDTO movie = client.getMovieByImdbID(apiKey, imdbID);
        validateMovie(movie);
        movieRepository.save(mapper.map(movie, Movie.class));
        return movie;
    }

    private void validateMovie(MovieDTO movie) {
        if (movie.getResponse().equals("False")) {
            throw new BusinessException(ResponseMessage.error(movie.getError()));
        }
    }

}
