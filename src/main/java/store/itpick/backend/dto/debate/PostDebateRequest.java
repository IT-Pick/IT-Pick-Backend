package store.itpick.backend.dto.debate;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "title은 필수입니다.")
    private String title;

    @NotBlank(message = "Debate는 필수입니다.")
    private String content;

    private List<VoteOptionRequest> voteOptions;
}

