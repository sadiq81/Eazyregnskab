package dk.eazyit.eazyregnskab.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
@Entity
@Table(name = "financeposting")
public class FinancePosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false, length = 50)
    private Date date;

    @Column(unique = false, nullable = false, length = 50)
    private String text;

    @Column(unique = false, nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(unique = false, nullable = false, length = 25)
    @Enumerated(EnumType.STRING)
    private FinancePostingStatus financePostingStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;

    public FinancePosting() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
