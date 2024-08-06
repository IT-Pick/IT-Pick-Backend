package store.itpick.backend.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "term_agreement")
public class TermAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agreement_id")
    private Long agreementId;

    @Column(name = "status", nullable = false, length = 10)
    private String status;

    @Column(name = "update_at", nullable = false)
    private Timestamp updateAt;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "agreement_type")
    private int agreementType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

