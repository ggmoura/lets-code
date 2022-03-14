package br.com.letscode.repository;

import br.com.letscode.entity.Movie;
import br.com.letscode.token.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface MovieRepository extends Repository<Movie, Long> {

    Movie save(Movie user);

    @Query(value = "SELECT u FROM Movie u", countQuery = "SELECT count(u) FROM Movie u")
    Page<Movie> findAll(Pageable pageable);

}
