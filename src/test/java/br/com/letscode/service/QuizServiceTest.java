package br.com.letscode.service;

import br.com.letscode.client.MovieClient;
import br.com.letscode.configuration.SharedPropertiesConfiguration;
import br.com.letscode.controller.model.movie.MovieDTO;
import br.com.letscode.controller.model.quiz.QuizStepRequest;
import br.com.letscode.controller.model.quiz.QuizStepResponse;
import br.com.letscode.entity.Movie;
import br.com.letscode.entity.Quiz;
import br.com.letscode.entity.QuizStep;
import br.com.letscode.entity.SelectedMovie;
import br.com.letscode.exception.BusinessException;
import br.com.letscode.repository.MovieRepository;
import br.com.letscode.repository.QuizRepository;
import br.com.letscode.repository.QuizStepRepository;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @InjectMocks
    private QuizService service;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private QuizStepRepository quizStepRepository;

    @Spy
    private ModelMapper mapper;

    @Mock
    private MovieClient client;

    @Mock
    private SharedPropertiesConfiguration properties;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Deve garantir que os metodos " +
            "userRepository.findByUsername, " +
            "quizRepository.findByUserAndFinished e " +
            "quizRepository.save, sejam invocados uma vez, em caso de sucesso")
    void startQuiz() {
        User user = new User();
        user.setPassword("teste");
        user.setPrivileges(new ArrayList<>());
        user.setName("teate");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.empty());
        when(quizRepository.save(any())).thenReturn(any());
        service.startQuiz("teste");
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(quizRepository, times(1)).findByUserAndFinished(any(), anyBoolean());
        verify(quizRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve levantar excecao BusinessException quando ja existir um quiz ativo")
    void startQuizException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        Quiz q = new Quiz();
        q.setId(1L);
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(q));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.startQuiz("teste");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Já existe um Quiz iniciado para o usuário {0}", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve garantir que os metodos " +
            "userRepository.findByUsername, " +
            "quizRepository.findByUserAndFinished, " +
            "quizRepository.save sejam executados uma vez cada")
    void finishQuiz() {
        User user = new User();
        user.setPassword("teste");
        user.setPrivileges(new ArrayList<>());
        user.setName("teate");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        Quiz quiz = new Quiz();
        quiz.setSteps(new ArrayList<>());
        quiz.getSteps().add(new QuizStep());
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any())).thenReturn(any());
        service.finishQuiz("quiz");
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(quizRepository, times(1)).findByUserAndFinished(any(), anyBoolean());
        verify(quizRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve levantar excecao BusinessException quando nao encontrar um quiz ativo")
    void finishQuizException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.finishQuiz("teste");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe um Quiz iniciado para o usuário {0}", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve levantar excecao BusinessException quando nao encontrar um quiz ativo")
    void nextStepException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.nextStep("teste");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe um Quiz iniciado para o usuário {0}", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve criar quizstep")
    void nextStep() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(new Quiz()));
        when(quizStepRepository.findByUserSelectedMovie(user)).thenReturn(Optional.empty());
        when(movieRepository.countMovies()).thenReturn(6L);
        Movie movie1 = new Movie();
        movie1.setId(1L);
        Movie movie2 = new Movie();
        movie2.setId(1L);
        Movie movie3 = new Movie();
        movie3.setId(2L);
        Movie movie4 = new Movie();
        movie4.setId(3L);
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1), Optional.of(movie2), Optional.of(movie3), Optional.of(movie4));
        service.nextStep("nextStep");
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(quizRepository, times(1)).findByUserAndFinished(any(), anyBoolean());
        verify(movieRepository, times(4)).findById(anyLong());

        movie1.setId(1L);
        movie2.setId(2L);
        movie3.setId(3L);
        movie4.setId(4L);
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1), Optional.of(movie2), Optional.of(movie3), Optional.of(movie4));
        when(quizStepRepository.getRespnsedQuizzesA(user)).thenReturn(Arrays.asList(1L));
        when(quizStepRepository.getRespnsedQuizzesB(user)).thenReturn(Arrays.asList(2L));
        service.nextStep("nextStep");
        verify(movieRepository, times(8)).findById(anyLong());

        movie1.setId(10L);
        movie2.setId(20L);
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie1), Optional.of(movie2));

        service.nextStep("nextStep");
        verify(movieRepository, times(10)).findById(anyLong());

    }

    @Test
    @DisplayName("Deve recuperar quizstep ja criado")
    void nextStepWithExists() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(new Quiz()));
        QuizStep quizStep = new QuizStep();
        Movie movieA = new Movie();
        movieA.setTitle("movieA");
        movieA.setImdbID("movieA");
        quizStep.setMovieA(movieA);
        Movie movieB = new Movie();
        movieB.setTitle("movieB");
        movieB.setImdbID("movieB");
        quizStep.setMovieB(movieB);
        when(quizStepRepository.findByUserSelectedMovie(any())).thenReturn(Optional.of(quizStep));
        QuizStepResponse resp = service.nextStep("nextStep");
        assertEquals("movieA", resp.getMovieA().getImdbID());
        assertEquals("movieA", resp.getMovieA().getTitle());
        assertEquals("movieB", resp.getMovieB().getImdbID());
        assertEquals("movieB", resp.getMovieB().getTitle());
    }

    @Test
    @DisplayName("Deve levantar excecao quanto tiver menos de seis filmes cadastrados, quantidade minima para permitir tres jogadas e finalizar automatico")
    void nextStepQuantityMoviesException() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(new Quiz()));
        when(quizStepRepository.findByUserSelectedMovie(user)).thenReturn(Optional.empty());
        when(movieRepository.countMovies()).thenReturn(5L);
        Movie movie = new Movie();
        movie.setId(1L);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.nextStep("nextStep");
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("É necessário que tenham mais de {0} filmes cadastrados", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve levantar excecao BusinessException quando nao encontrar um quiz ativo")
    void responseQuizExceptionQuiz() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.responseQuiz("teste", any());
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe um Quiz iniciado para o usuário {0}", exception.getErrors().get(0).getText());
    }

    @Test
    @DisplayName("Deve levantar excecao BusinessException quando nao encontrar uma step criada")
    void responseQuizExceptionQuizStep() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(new Quiz()));
        when(quizStepRepository.findByUserSelectedMovie(new User())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.responseQuiz("teste", any());
        });
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Não existe um Step criada para o quiz", exception.getErrors().get(0).getText());
    }

    @Test
    void responseQuiz() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(properties.getApiKey()).thenReturn("somewayKey");
        Quiz q = new Quiz();
        q.setFinished(Boolean.FALSE);
        q.setId(1L);
        q.setScore(0);
        q.setQtdSteps(0);
        when(quizRepository.findByUserAndFinished(any(), anyBoolean())).thenReturn(Optional.of(q));
        QuizStep quizStep = new QuizStep();
        Movie movieA = new Movie();
        movieA.setImdbID("movieA");
        quizStep.setMovieA(movieA);
        Movie movieB = new Movie();
        movieB.setImdbID("movieB");
        quizStep.setMovieB(movieB);
        when(quizStepRepository.findByUserSelectedMovie(user)).thenReturn(Optional.of(quizStep));
        MovieDTO movieDTOA = new MovieDTO();
        movieDTOA.setImdbRating("100");
        movieDTOA.setImdbVotes("10");
        MovieDTO movieDTOB = new MovieDTO();
        movieDTOB.setImdbRating("30");
        movieDTOB.setImdbVotes("10");
        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTOA, movieDTOB);

        QuizStepRequest request = new QuizStepRequest();
        request.setSelectedMovie(SelectedMovie.A);

        QuizStepResponse response = service.responseQuiz("responseQuiz", request);
        assertTrue(response.getRightAnswer());
        assertThat(1, is(q.getQtdSteps()));
        assertThat(1, is(q.getScore()));

        movieDTOB.setImdbRating("100");
        movieDTOB.setImdbVotes("10");
        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTOA, movieDTOB);

        response = service.responseQuiz("responseQuiz", request);
        assertTrue(response.getRightAnswer());

        movieDTOB.setImdbRating("1000");
        movieDTOB.setImdbVotes("10");
        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTOA, movieDTOB);

        response = service.responseQuiz("responseQuiz", request);
        assertFalse(response.getRightAnswer());

        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTOA, movieDTOB);
        when(quizStepRepository.countWrongAnswers(user)).thenReturn(4L);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            service.responseQuiz("responseQuiz", request);
        });
        assertTrue(q.getFinished());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Você errou qual filme possui maior pontuação 3 vezes, o Quiz foi finalizado automaticamente, " +
                "sua porcentagem de acertos foi {0}", exception.getErrors().get(0).getText());


        movieDTOA.setImdbRating("N/A");
        movieDTOA.setImdbVotes("N/A");

        movieDTOB.setImdbRating("N/A");
        movieDTOB.setImdbVotes("N/A");

        when(client.getMovieByImdbID(anyString(), anyString())).thenReturn(movieDTOA, movieDTOB);
        exception = assertThrows(BusinessException.class, () -> {
            service.responseQuiz("responseQuiz", request);
        });
        assertTrue(q.getFinished());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getHttpStatus());
        assertThat(exception.getErrors().size(), is(1));
        assertEquals("Filme sem dados suficientes na plataforma {0}, ImdbRating={1}, ImdbVotes={2}",
                exception.getErrors().get(0).getText());



    }

    @Test
    void getRanking() {
        when(quizRepository.getRanking(Pageable.unpaged())).thenReturn(new ArrayList<>());
        service.getRanking(Pageable.unpaged());
        verify(quizRepository, times(1)).getRanking(Pageable.unpaged());
    }

}