package br.com.letscode.token.repository;

import br.com.letscode.controller.model.quiz.QuizRankingResponse;
import br.com.letscode.controller.model.user.UserResponse;
import br.com.letscode.token.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

	Optional<User> findById(Long id);

	Optional<User> findByUsername(String username);

	@Query(value = "SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByLogin(String username);

	User save(User user);

	@Query(value = "SELECT u FROM User u", countQuery = "SELECT count(u) FROM User u")
	Page<User> findAll(Pageable pageable);

	@Query(value = "SELECT u FROM User u  ORDER BY u.totalSteps  DESC, u.totalScore  DESC",
			countQuery = "SELECT count(u) FROM User u ORDER BY u.totalSteps  DESC, u.totalScore  DESC")
	List<User> getRanking(Pageable pageable);

}
