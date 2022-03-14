package br.com.letscode.repository;


import br.com.letscode.controller.model.quiz.QuizRankingResponse;
import br.com.letscode.entity.Quiz;
import br.com.letscode.token.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends Repository<Quiz, Long> {

    Quiz save(Quiz quiz);

    Optional<Quiz> findByUserAndFinished(User user, Boolean finished);

    @Query(value = "SELECT m FROM Quiz m", countQuery = "SELECT count(m) FROM Movie m")
    List<QuizRankingResponse> getRanking(Pageable pageable);

}
