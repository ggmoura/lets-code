package br.com.letscode.service;

import br.com.letscode.client.MovieClient;
import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.configuration.SharedPropertiesConfiguration;
import br.com.letscode.controller.model.movie.MovieDTO;
import br.com.letscode.controller.model.movie.MovieResponse;
import br.com.letscode.controller.model.quiz.QuizRankingResponse;
import br.com.letscode.controller.model.quiz.QuizResponse;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private QuizStepRepository quizStepRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MovieClient client;

    @Autowired
    private SharedPropertiesConfiguration properties;

    public void startQuiz(String username) {
        User user = userRepository.findByUsername(username).get();
        Quiz quiz = quizRepository.findByUserAndFinished(user, Boolean.FALSE).orElse(new Quiz());
        if (quiz.getId() == null) {
            quiz.setUser(user);
            quiz.setScore(0);
            quiz.setQtdSteps(0);
            quiz.setFinished(Boolean.FALSE);
            quizRepository.save(quiz);
        } else {
            throw new BusinessException(ResponseMessage.error("Já existe um Quiz iniciado para o usuário {0}", username));
        }
    }

    private Movie getRandomMovie(List<Long> excludes) {
        Long qtdMovies = movieRepository.countMovies();
        Random random = new Random();
        return getRandomMovie(excludes, qtdMovies, random);
    }

    private Movie getRandomMovie(List<Long> excludes, Long maxResults, Random random) {
        Integer idMovie = random.nextInt(maxResults.intValue());
        if (idMovie.equals(0)) {
            idMovie++;
        }
        if (excludes.contains(idMovie)) {
            return getRandomMovie(excludes, maxResults, random);
        }
        return movieRepository.findById(idMovie.longValue()).get();
    }

    public QuizResponse finishQuiz(String username) {
        User user = userRepository.findByUsername(username).get();
        Quiz quiz = quizRepository.findByUserAndFinished(user, Boolean.FALSE).orElseThrow(() ->
                new BusinessException(ResponseMessage.error("Não existe um Quiz iniciado para o usuário {0}", username)));
        quiz.setFinished(Boolean.TRUE);
        quizRepository.save(quiz);
        return mapper.map(quiz, QuizResponse.class);
    }

    public QuizStepResponse nextStep(String username) {
        User user = userRepository.findByUsername(username).get();
        Quiz quiz = quizRepository.findByUserAndFinished(user, Boolean.FALSE).orElseThrow(() ->
                new BusinessException(ResponseMessage.error("Não existe um Quiz iniciado para o usuário {0}", username)));
        Optional<QuizStep> quizStepOptional = quizStepRepository.findByUserSelectedMovie(user);
        QuizStepResponse response = new QuizStepResponse();
        if (quizStepOptional.isEmpty()) {
            List<Long> excludedMovies = quizStepRepository.getRespnsedQuizzesB(user);
            Movie movieA = getRandomMovie(excludedMovies);
            excludedMovies = quizStepRepository.getRespnsedQuizzesA(user);
            excludedMovies.add(movieA.getId());
            Movie movieB = getRandomMovie(excludedMovies);
            QuizStep quizStep = new QuizStep();
            quizStep.setMovieA(movieA);
            quizStep.setMovieB(movieB);
            quizStep.setQuiz(quiz);
            quizStepRepository.save(quizStep);
            response.setMovieA(mapper.map(movieA, MovieResponse.class));
            response.setMovieB(mapper.map(movieB, MovieResponse.class));
        } else {
            QuizStep step = quizStepOptional.get();
            response.setMovieA(mapper.map(step.getMovieA(), MovieResponse.class));
            response.setMovieB(mapper.map(step.getMovieB(), MovieResponse.class));
        }
        return response;
    }
    public QuizStepResponse responseQuiz(String username, QuizStepRequest request) {

        User user = userRepository.findByUsername(username).get();
        Quiz quiz = quizRepository.findByUserAndFinished(user, Boolean.FALSE).orElseThrow(() ->
                new BusinessException(ResponseMessage.error("Não existe um Quiz iniciado para o usuário {0}", username)));
        QuizStep quizStep = quizStepRepository.findByUserSelectedMovie(user).orElseThrow(() ->
                new BusinessException(ResponseMessage.error("Não existe um Step criada para o quiz")));

        MovieDTO movieA = client.getMovieByImdbID(properties.getApiKey(), quizStep.getMovieA().getImdbID());
        MovieDTO movieB = client.getMovieByImdbID(properties.getApiKey(), quizStep.getMovieB().getImdbID());

        Double scoreMoveA = calculateScore(movieA);
        Double scoreMoveB = calculateScore(movieB);
        if (scoreMoveA == scoreMoveB) {
            quizStep.setRightAnswer(Boolean.TRUE);
        } else if (scoreMoveA > scoreMoveB) {
            quizStep.setRightAnswer(request.getSelectedMovie() == SelectedMovie.A);
        } else {
            quizStep.setRightAnswer(request.getSelectedMovie() == SelectedMovie.B);
        }
        quizStep.setSelectedMovie(request.getSelectedMovie());
        quizStepRepository.save(quizStep);
        quiz.setQtdSteps(quiz.getQtdSteps() + 1);
        if (quizStep.getRightAnswer()) {
            quiz.setScore(quiz.getScore() + 1);
        } else {
            Long wrongAnswers = quizStepRepository.countWrongAnswers(user);
            if (wrongAnswers >= 3) {
                quiz.setFinished(Boolean.TRUE);
            }
        }
        quizRepository.save(quiz);
        if (quiz.getFinished()) {
            throw new BusinessException(
                    "Você errou qual filme possui maior pontuação 3 vezes, o Quiz foi finalizado automaticamente, " +
                            "sua porcentagem de acertos foi {0}", String.valueOf(quiz.getScore().doubleValue() / quiz.getQtdSteps()));
        }
        return mapper.map(quizStep, QuizStepResponse.class);
    }

    private Double calculateScore(MovieDTO movie) {
        if (movie.getImdbRating().equals("N/A") || movie.getImdbVotes().equals("N/A")) {
            throw new BusinessException(ResponseMessage.error(
                    "Filme sem dados suficientes na plataforma {0}, ImdbRating={1}, ImdbVotes={2}",
                    "http://www.omdbapi.com", movie.getImdbRating(), movie.getImdbVotes()));
        }
        Double imdbRating = Double.parseDouble(movie.getImdbRating());
        Integer imdbVotes = Integer.parseInt(movie.getImdbVotes().replaceAll("[^\\d.]", ""));
        return  imdbVotes * imdbRating;
    }

    public List<QuizRankingResponse> getRanking(Pageable pageable) {
        return quizRepository.getRanking(pageable);
    }
}
