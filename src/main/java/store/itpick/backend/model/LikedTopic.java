package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "liked_topic")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikedTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_topic_id")
    private Long likedTopicId;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "liked_topic")
    private String liked_topic;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
