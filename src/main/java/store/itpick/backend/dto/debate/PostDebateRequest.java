package store.itpick.backend.dto.debate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDebateRequest {
    private String title;
    private String content;
    private Long voteNum;
}
