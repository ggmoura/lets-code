package br.com.letscode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MOVIE", indexes = {
        @Index(name = "title", unique = true, columnList = "title")
})
public class Movie extends BaseEntity<Long> {

    @NotNull
    @Column(name = "title", nullable = false)
    public String title;

    @NotNull
    @Column(name = "imdbID", nullable = false)
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