package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "vote_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long voteOptionId;

    @Column(name = "option_text", nullable = false, length = 255)
    private String optionText;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Lob
    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @ManyToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;
}
