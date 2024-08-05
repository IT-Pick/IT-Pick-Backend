package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "debate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Debate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debate_id")
    private Long debateId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "hits", nullable = false)
    private Long hits;

    @Column(name = "on_trend", nullable = false)
    private boolean onTrend;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;
}
