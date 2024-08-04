package store.itpick.backend.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RankingWeight {
    NAVER(50),
    NATE(30),
    ZUM(20);

    private final int rankingWeight;

    public int get() {
        return rankingWeight;
    }
}
