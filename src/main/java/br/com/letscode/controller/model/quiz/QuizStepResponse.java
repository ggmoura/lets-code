package br.com.letscode.controller.model.quiz;


import br.com.letscode.controller.model.movie.MovieResponse;
import br.com.letscode.entity.SelectedMovie;

public class QuizStepResponse {

    private MovieResponse movieA;

    private MovieResponse movieB;

    private Boolean rightAnswer;

    private SelectedMovie selectedMovie;

    public MovieResponse getMovieA() {
        return movieA;
    }

    public void setMovieA(MovieResponse movieA) {
        this.movieA = movieA;
    }

    public MovieResponse getMovieB() {
        return movieB;
    }

    public void setMovieB(MovieResponse movieB) {
        this.movieB = movieB;
    }

    public Boolean getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Boolean rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public SelectedMovie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(SelectedMovie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
}
