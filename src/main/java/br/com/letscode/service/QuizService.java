package br.com.letscode.service;

import br.com.letscode.controller.model.quiz.QuizQuestionRequest;
import br.com.letscode.controller.model.quiz.QuizQuestionResponse;
import br.com.letscode.controller.model.quiz.QuizResponse;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    public void startQuiz(String name) {

    }

    public QuizResponse finishQuiz(String name) {
        return  null;
    }

    public QuizQuestionResponse nextStep(String name) {
        return  null;
    }
    public QuizQuestionResponse responseQuiz(String name, QuizQuestionRequest request) {
        return  null;
    }

}
