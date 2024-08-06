package store.itpick.backend.dto.debate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostDebateResponse {
    private long debateId;
}
