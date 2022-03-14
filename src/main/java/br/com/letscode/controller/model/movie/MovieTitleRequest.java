package br.com.letscode.controller.model.movie;

import javax.validation.constraints.NotNull;

public class MovieTitleRequest {

    @NotNull
    public String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
