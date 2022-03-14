package br.com.letscode.repository;

import br.com.letscode.entity.Movie;
import br.com.letscode.token.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MovieRepository extends Repository<Movie, Long> {

    Movie save(Movie movie);

    @Query(value = "SELECT m FROM Movie m", countQuery = "SELECT count(m) FROM Movie m")
    Page<Movie> findAll(Pageable pageable);

    @Query(value = "SELECT count(m) FROM Movie m")
    Long countMovies();

    Optional<Movie> findById(Long id);

}
