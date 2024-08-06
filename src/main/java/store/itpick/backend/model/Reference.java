package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "reference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "search_link", nullable = false, length = 255)
    private String searchLink;

    @Column(name = "news_title", nullable = false, length = 100)
    private String newsTitle;

    @Column(name = "news_image", nullable = false, length = 255)
    private String newsImage;

    @Column(name = "news_content", nullable = false, length = 255)
    private String newsContent;

    @Column(name = "news_link", nullable = false, length = 255)
    private String newsLink;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "active"; // 기본값 'active'

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createAt = now;
        this.updateAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = Timestamp.from(Instant.now());
    }
}
