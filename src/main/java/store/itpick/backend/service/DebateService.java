package store.itpick.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.itpick.backend.dto.debate.PostDebateRequest;
import store.itpick.backend.dto.debate.PostDebateResponse;
import store.itpick.backend.dto.vote.PostVoteRequest;
import store.itpick.backend.model.Debate;
import store.itpick.backend.repository.DebateRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebateService {

    private final DebateRepository debateRepository;
    private final VoteService voteService;

    @Transactional
    public PostDebateResponse createDebate(PostDebateRequest postDebateRequest) {
        Debate debate = Debate.builder()
                .title(postDebateRequest.getTitle())
                .content(postDebateRequest.getContent())
                .hits(0L)
                .onTrend(false)
                .status("active")
                .createAt(Timestamp.valueOf(LocalDateTime.now()))
                .updateAt(Timestamp.valueOf(LocalDateTime.now()))
                .voteNum(postDebateRequest.getVoteNum())
                .build();

        debate = debateRepository.save(debate);

        if (postDebateRequest.getVoteNum() > 0) {
            PostVoteRequest postVoteRequest = new PostVoteRequest();
            postVoteRequest.setDebateId(debate.getDebateId());
            postVoteRequest.setOptionNum(postDebateRequest.getVoteNum());
            voteService.createVote(postVoteRequest);
        }

        return new PostDebateResponse(debate.getDebateId());
    }
}
