package store.itpick.backend.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommunityType {
    TOTAL("total"),
    NAVER("naver"),
    NATE("nate"),
    ZUM("zum");

    private final String communityType;

    public String get() {
        return communityType;
    }
}
