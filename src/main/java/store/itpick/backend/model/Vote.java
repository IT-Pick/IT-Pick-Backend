package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @OneToOne
    @JoinColumn(name = "debate_id", nullable = false)
    private Debate debate;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteOption> voteOptions;
}
