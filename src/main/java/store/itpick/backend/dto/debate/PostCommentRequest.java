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
public class PostCommentRequest {

    @NotBlank(message = "comment는 필수입니다.")
    private String comment;

    private Long parentCommentId;

    private Long debateId;

    private Long userId;
}

