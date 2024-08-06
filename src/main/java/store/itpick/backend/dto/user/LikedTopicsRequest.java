package store.itpick.backend.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class LikedTopicsRequest {
    private List<String> likedTopicList;
}
