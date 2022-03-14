package br.com.letscode.token.repository;

import br.com.letscode.token.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

	Optional<User> findById(Long id);

	@Query(value = "SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByLogin(String username);

	User save(User user);

}
