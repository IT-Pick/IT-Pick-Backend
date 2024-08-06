package store.itpick.backend.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetRankingListResponse {
    private String redisKey;
    private List<String> rankingList;
}
