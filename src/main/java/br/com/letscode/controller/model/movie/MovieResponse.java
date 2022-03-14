package br.com.letscode.controller.model.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieResponse {

    @JsonProperty("Title")
    public String title;

    public String imdbID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
