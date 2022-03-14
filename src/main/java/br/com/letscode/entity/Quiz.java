package br.com.letscode.entity;

import br.com.letscode.token.entity.User;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "QUIZ")
public class Quiz extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<QuizStep> steps;

    @Column(name = "score")
    private Integer score;

    @Column(name = "finished")
    private Boolean finished;

    public User getUser() {
        return user;
    }

    public Integer getScore() {
        return score;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<QuizStep> getSteps() {
        return steps;
    }

    public void setSteps(List<QuizStep> steps) {
        this.steps = steps;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
