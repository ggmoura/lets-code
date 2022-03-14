package br.com.letscode.listener;

import br.com.letscode.entity.Movie;
import br.com.letscode.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitialMovieLoaderListener {

    @Autowired
    private MovieService service;

    @Autowired
    private MovieProperties properties;

    @Value(value = "${letscode.authentication.mustbeinitialize.movies:true}")
    private boolean mustBeInitialize;

    @Autowired
    private TaskExecutor taskExecutor;


    @EventListener
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.mustBeInitialize) {
            updateMovies();
            this.mustBeInitialize = Boolean.FALSE;
        }
    }

    private void updateMovies() {
        taskExecutor.execute(() -> {
            properties.getMovies().parallelStream().forEach(movie -> service.createMovieByImdbID(movie));
        });
    }

}
