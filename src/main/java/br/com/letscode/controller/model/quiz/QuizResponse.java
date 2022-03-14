package br.com.letscode.controller.model.quiz;

import java.util.List;

public class QuizResponse {

    private List<QuizStepResponse> steps;

    private Integer score;

    private Boolean finished;

    public List<QuizStepResponse> getSteps() {
        return steps;
    }

    public void setSteps(List<QuizStepResponse> steps) {
        this.steps = steps;
    }

    public Integer getScore() {
        return score;
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
