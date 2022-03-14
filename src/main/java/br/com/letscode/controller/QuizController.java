package br.com.letscode.controller;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import br.com.letscode.controller.model.quiz.QuizRankingResponse;
import br.com.letscode.controller.model.quiz.QuizResponse;
import br.com.letscode.controller.model.quiz.QuizStepRequest;
import br.com.letscode.controller.model.quiz.QuizStepResponse;
import br.com.letscode.security.UserPrincipal;
import br.com.letscode.service.QuizService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RolesAllowed("PLAYER")
@RestController
@RequestMapping(value = "quizzes")
public class QuizController {

    @Autowired
    private QuizService service;

    @GetMapping("/start")
    public ResponseObject<Void> start(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user) {
        service.startQuiz(user.getUsername());
        return ResponseObject.message(ResponseMessage.success("Quiz iniciado com sucesso"));
    }

    @GetMapping("finish")
    public ResponseObject<QuizResponse> finish(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user) {
        QuizResponse response = service.finishQuiz(user.getUsername());
        return ResponseObject.of(response, ResponseMessage.success("Quiz finalizado com sucesso"));
    }

    @GetMapping("next-step")
    public ResponseObject<QuizStepResponse> nextStep(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user) {
        QuizStepResponse response = service.nextStep(user.getUsername());
        return ResponseObject.of(response);
    }

    @PostMapping("response")
    public ResponseObject<QuizStepResponse> responseQuiz(
            @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody QuizStepRequest request) {
        QuizStepResponse response = service.responseQuiz(user.getUsername(), request);
        return ResponseObject.of(response);
    }

    @RolesAllowed({"MANAGER", "PLAYER"})
    @GetMapping("ranking")
    public ResponseObject<QuizRankingResponse> ranking(@Parameter(hidden = true) Pageable pageable) {
        List<QuizRankingResponse> response = service.getRanking(pageable);
        return ResponseObject.page(response);
    }

}
