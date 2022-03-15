package br.com.letscode.service;

import br.com.letscode.client.MovieClient;
import br.com.letscode.configuration.SharedPropertiesConfiguration;
import br.com.letscode.controller.model.movie.MovieDTO;
import br.com.letscode.entity.Movie;
import br.com.letscode.exception.BusinessException;
import br.com.letscode.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @InjectMocks
    private MovieService service;

    @Mock
    private MovieClient client;

    @Mock
    private MovieRepository movieRepository;

    @Spy
    private ModelMapper mapper;

    @Mock
    private SharedPropertiesConfiguration properties;

    @BeforeEach
    void setUp() {
        when(properties.getApiKey()).thenReturn("somewayKey");
    }

    @Test
    void createMovieByTytle() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setImdbVotes("10");
        movieDTO.setImdbVotes("100");
        movieDTO.setResponse("");

        when(client.getMovieByTitle(anyString(), anyString())).thenReturn(movieDTO);
        when(movieRepository.save(any())).thenReturn(new Movie());

        service.createMovieByTytle("title");

        verify(client, times(1)).getMovieByTitle(anyString(), anyString());
        verify(movieRepository, times(1)).save(any());
    }

    @Test
    void createMovieByTytleResponseFalse() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setResponse("False");
        movieDTO.setError("Someway Error");
        when(client.getMovieByTitle(anyString(), anyString())).thenReturn(movieDTO);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.createMovieByTytle("title");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals(movieDTO.getError(), exception.getErrors().get(0).getText());
    }


    @Test
    void createMovieByTytleImdbRatingNA() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setResponse("");
        movieDTO.setImdbRating("N/A");
        when(client.getMovieByTitle(anyString(), anyString())).thenReturn(movieDTO);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.createMovieByTytle("title");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Filme sem dados suficientes na plataforma {0}, ImdbRating={1}, ImdbVotes={2}",
                exception.getErrors().get(0).getText());
    }

    @Test
    void createMovieByImdbID() {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setImdbVotes("10");
        movieDTO.setImdbVotes("100");
        movieDTO.setResponse("");

        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTO);
        when(movieRepository.save(any())).thenReturn(new Movie());

        service.createMovieByImdbID("title");

        verify(client, times(1)).getMovieByImdbID(anyString(), anyString());
        verify(movieRepository, times(1)).save(any());
    }
}