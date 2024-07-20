package store.itpick.backend.dto.user.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PostUserResponse {
    private UUID userId;
    private String jwt;


}
