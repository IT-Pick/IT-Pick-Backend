package store.itpick.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import store.itpick.backend.common.exception.VoteException;
import store.itpick.backend.dto.debate.VoteOptionRequest;
import store.itpick.backend.dto.vote.PostVoteRequest;
import store.itpick.backend.dto.vote.PostVoteResponse;
import store.itpick.backend.model.Debate;
import store.itpick.backend.model.Vote;
import store.itpick.backend.model.VoteOption;
import store.itpick.backend.repository.DebateRepository;
import store.itpick.backend.repository.VoteRepository;
import store.itpick.backend.repository.VoteOptionRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


import static store.itpick.backend.common.response.status.BaseExceptionResponseStatus.INVALID_DEBATE_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final DebateRepository debateRepository;
    private final VoteOptionRepository voteOptionRepository;

    @Transactional
    public PostVoteResponse createVote(PostVoteRequest postVoteRequest, List<VoteOptionRequest> voteOptions) {
        Debate debate = debateRepository.findById(postVoteRequest.getDebateId())
                .orElseThrow(() -> new VoteException(INVALID_DEBATE_ID));

        Vote vote = Vote.builder()
                .status("active")
                .createAt(Timestamp.valueOf(LocalDateTime.now()))
                .updateAt(Timestamp.valueOf(LocalDateTime.now()))
                .debate(debate)
                .build();

        vote = voteRepository.save(vote);

        if (voteOptions != null && !voteOptions.isEmpty()) {
            createVoteOptions(vote, voteOptions);
        }

        return new PostVoteResponse(vote.getVoteId());
    }

    @Transactional
    public void createVoteOptions(Vote vote, List<VoteOptionRequest> voteOptions) {
        for (VoteOptionRequest option : voteOptions) {
            VoteOption voteOption = VoteOption.builder()
                    .optionText(option.getOptionText())
                    .imgUrl(option.getImgUrl())
                    .status("active")
                    .createAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updateAt(Timestamp.valueOf(LocalDateTime.now()))
                    .vote(vote)
                    .build();

            voteOptionRepository.save(voteOption);
        }
    }
}
