package store.itpick.backend.dto.debate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDebateRequest {
    private String title;
    private String content;
    private List<VoteOptionRequest> voteOptions;
}

