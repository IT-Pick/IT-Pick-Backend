package store.itpick.backend.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetRankingBadgeResponse {
    private Long nateRank;
    private Long naverRank;
    private Long zumRank;
}
