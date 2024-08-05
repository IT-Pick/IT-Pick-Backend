package store.itpick.backend.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RankingWeight {
    NAVER(5),
    NATE(3),
    ZUM(2);

    private final int rankingWeight;

    public int get() {
        return rankingWeight;
    }
}
