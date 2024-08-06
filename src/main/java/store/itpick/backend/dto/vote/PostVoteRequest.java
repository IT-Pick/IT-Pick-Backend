package store.itpick.backend.dto.vote;

import lombok.Data;

@Data
public class PostVoteRequest {
    private Long debateId;
    private Long optionNum;
}
