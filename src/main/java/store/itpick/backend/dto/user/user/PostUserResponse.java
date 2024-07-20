package store.itpick.backend.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostUserResponse {
    private long userId;
    private String jwt;


}
