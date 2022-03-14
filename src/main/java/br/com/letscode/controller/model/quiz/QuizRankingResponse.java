package br.com.letscode.controller.model.quiz;

import br.com.letscode.token.entity.User;

public class QuizRankingResponse {

    private User user;
    private Integer position;
    private Double score;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
