package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "keyword")
@Getter
@Setter
@NoArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(name = "keyword", nullable = false, length = 50)
    private String keyword;


    @Column(name = "status", nullable = false, length = 20)
    private String status = "active"; // 기본값 'active'

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;


    // 외래키 설정
    @ManyToOne
    @JoinColumn(name = "reference_id")  // 외래키 컬럼 이름
    private Reference reference;

    @ManyToMany
    @JoinTable(
            name = "keyword_community_period",
            joinColumns = @JoinColumn(name = "keyword_id"),
            inverseJoinColumns = @JoinColumn(name = "community_period_id")
    )
    private List<CommunityPeriod> communityPeriods = new ArrayList<>();

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
