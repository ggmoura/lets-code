package br.com.letscode.controller;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import br.com.letscode.controller.model.movie.MovieDTO;
import br.com.letscode.controller.model.movie.MovieImdbIDRequest;
import br.com.letscode.controller.model.movie.MovieTitleRequest;
import br.com.letscode.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RolesAllowed("MANAGER")
@RestController
@RequestMapping(value = "movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/title")
    public ResponseObject<MovieDTO> createMovieByTitle(@Valid @RequestBody MovieTitleRequest request) {
        MovieDTO response = service.createMovieByTytle(request.getTitle());
        return ResponseObject.of(response, ResponseMessage.success("Movie {0} saved", response.getTitle()));
    }


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/imdbid")
    public ResponseObject<MovieDTO> createMovieByTitle(@Valid @RequestBody MovieImdbIDRequest request) {
        MovieDTO response = service.createMovieByImdbID(request.getImdbID());
        return ResponseObject.of(response, ResponseMessage.success("Movie {0} saved", response.getTitle()));
    }

}
