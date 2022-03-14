package br.com.letscode.entity;

import javax.persistence.*;


@Entity
@Table(name = "QUIZ_STEP", indexes = {
        @Index(name = "quiz", unique = true, columnList = "quiz, movie_a, movie_b")
})
public class QuizStep extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "quiz")
    private Quiz quiz;

    @JoinColumn(name = "movie_a")
    @ManyToOne
    private Movie movieA;

    @JoinColumn(name = "movie_b")
    @ManyToOne
    private Movie movieB;

    @Column(name = "right_answer")
    private Boolean rightAnswer;

    @Column(name = "selected_movie", length = 1)
    @Enumerated(EnumType.STRING)
    private SelectedMovie selectedMovie;

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Movie getMovieA() {
        return movieA;
    }

    public void setMovieA(Movie movieA) {
        this.movieA = movieA;
    }

    public Movie getMovieB() {
        return movieB;
    }

    public void setMovieB(Movie movieB) {
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
