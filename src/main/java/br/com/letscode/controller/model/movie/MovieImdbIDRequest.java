package br.com.letscode.controller.model.movie;

import javax.validation.constraints.NotNull;

public class MovieImdbIDRequest {

    @NotNull
    public String imdbID;

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
