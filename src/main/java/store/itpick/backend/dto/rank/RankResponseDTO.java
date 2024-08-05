package store.itpick.backend.dto.rank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankResponseDTO {
    private String keyword;
    private String searchLink;
    private String newsTitle;
    private String newsContent;
    private String newsLink;
    private String imageUrl;

    public RankResponseDTO() {}

    public RankResponseDTO(String keyword, String searchLink, String newsTitle, String newsContent, String newsLink, String imageUrl) {
        this.keyword = keyword;
        this.searchLink = searchLink;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsLink = newsLink;
        this.imageUrl = imageUrl;
    }
}
