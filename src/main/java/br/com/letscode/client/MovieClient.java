package br.com.letscode.client;

import br.com.letscode.configuration.FeignConfiguration;
import br.com.letscode.controller.model.movie.MovieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static br.com.letscode.commons.Constant.FeignClientName.OMDBAPI_MOVIE;

@FeignClient(name = OMDBAPI_MOVIE, url = "${client.omdbapi.url}", configuration = FeignConfiguration.class)
public interface MovieClient {

    @GetMapping
    MovieDTO getMovieByTitle(@RequestParam String apikey, @RequestParam(name = "t") String title);

    @GetMapping
    MovieDTO getMovieByImdbID(@RequestParam String apikey, @RequestParam(name = "i") String imdbID);

}
