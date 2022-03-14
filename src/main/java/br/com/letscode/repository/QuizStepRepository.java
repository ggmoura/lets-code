package br.com.letscode.repository;

import br.com.letscode.entity.QuizStep;
import br.com.letscode.token.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface QuizStepRepository extends Repository<QuizStep, Long> {

    QuizStep save(QuizStep quizRsponse);

    @Query(value = "SELECT s.movieA.id FROM QuizStep s WHERE s.quiz.user = :user and s.quiz.finished is false")
    List<Long> getRespnsedQuizzesA(User user);

    @Query(value = "SELECT s.movieB.id FROM QuizStep s WHERE s.quiz.user = :user and s.quiz.finished is false")
    List<Long> getRespnsedQuizzesB(User user);

    @Query(value = "SELECT s FROM QuizStep s WHERE s.quiz.user = :user and s.selectedMovie is null")
    Optional<QuizStep> findByUserSelectedMovie(User user);

    @Query(value = "SELECT count(s) FROM QuizStep s WHERE s.quiz.finished is false and s.quiz.user = :user and s.rightAnswer is false")
    Long countWrongAnswers(User user);

}
