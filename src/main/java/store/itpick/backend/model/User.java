package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long birth_date;

    @Column
    private Boolean alect_setting;

    @Column
    private String reference_code;

    @Column
    private String image_url;

    @Column
    private String refresh_token;

    @Column
    private String status;

    @Column
    private Timestamp create_at;

    @Column
    private Timestamp update_at;
}
