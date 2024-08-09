package store.itpick.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CommunityPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityPeriodId;

    private String community;
    private String period;

    @ManyToMany(mappedBy = "communityPeriods")
    private List<Keyword> keywords = new ArrayList<>();

}