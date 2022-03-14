package br.com.letscode.controller.model.quiz;

import br.com.letscode.entity.SelectedMovie;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class QuizStepRequest {

    @NotNull
    @JsonProperty
    private SelectedMovie selectedMovie;

    public SelectedMovie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(SelectedMovie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
}
