package store.itpick.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.itpick.backend.common.exception.DebateException;
import store.itpick.backend.common.response.BaseResponse;
import store.itpick.backend.dto.debate.PostCommentRequest;
import store.itpick.backend.dto.debate.PostCommentResponse;
import store.itpick.backend.dto.debate.PostDebateRequest;
import store.itpick.backend.dto.debate.PostDebateResponse;
import store.itpick.backend.service.DebateService;

import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_COMMENT_VALUE;
import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_DEBATE_VALUE;
import static store.itpick.backend.util.BindingResultUtils.getErrorMessages;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/debate")
public class DebateController {

    private final DebateService debateService;

    @PostMapping("")
    public BaseResponse<PostDebateResponse> createDebate(@Valid @RequestBody PostDebateRequest postDebateRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new DebateException(INVALID_DEBATE_VALUE, getErrorMessages(bindingResult));
        }

        return new BaseResponse<>(debateService.createDebate(postDebateRequest));
    }

    @PostMapping("/comment")
    public BaseResponse<PostCommentResponse> createComment(@Valid @RequestBody PostCommentRequest postCommentRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new DebateException(INVALID_COMMENT_VALUE, getErrorMessages(bindingResult));
        }

        return new BaseResponse<>(debateService.createComment(postCommentRequest));
    }
}
