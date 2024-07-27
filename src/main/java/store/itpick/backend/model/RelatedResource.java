package store.itpick.backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatedResource {
    private String keywords;
    private String searchLink;
    private String newsTitle;
    private String newsContent;
    private String newsLink;
    private String imageUrl;

    public RelatedResource(String keywords, String searchLink, String newsTitle, String newsContent, String newsLink, String imageUrl) {
        this.keywords = keywords;
        this.searchLink = searchLink;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.newsLink = newsLink;
        this.imageUrl = imageUrl;
    }
}
