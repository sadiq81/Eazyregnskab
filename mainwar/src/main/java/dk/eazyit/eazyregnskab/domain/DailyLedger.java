package dk.eazyit.eazyregnskab.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, query = "select dl from DailyLedger dl where dl.legalEntity= ?1")
})
@Table(name = "dailyledger")
public class DailyLedger extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "DailyLedger::findByLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,length = 20)
    private String name;

    @ManyToOne(optional = false)
    private LegalEntity legalEntity;

    @OneToMany(mappedBy = "dailyLedger", fetch = FetchType.LAZY)
    private Set<DraftFinancePosting> draftFinancePosting;

    public DailyLedger() {
    }

    public DailyLedger(String name, LegalEntity legalEntity) {
        this.name = name;
        this.legalEntity = legalEntity;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public DailyLedger setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        return this;
    }

    public Set<DraftFinancePosting> getDraftFinancePosting() {
        return draftFinancePosting;
    }

    public void setDraftFinancePosting(Set<DraftFinancePosting> draftFinancePosting) {
        this.draftFinancePosting = draftFinancePosting;
    }

    @Override
    public String toString() {
        return "DailyLedger{" +
                "id=" + id +
                ", legalEntity=" + legalEntity.getName() +
                '}';
    }
}
