package store.itpick.backend.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "rank")
@Getter
@AllArgsConstructor
public class Rank {
    @Id
    private String key; // COMMUNITY_DATE   e.g.) NAMUWIKI_240730
    private String keyword; // e.g.) 남자 양궁
    private String rank;    // e.g.) 1
}
