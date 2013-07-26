package dk.eazyit.eazyregnskab.domain;

import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.persistence.*;
import java.util.Date;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, query = "select fy from FiscalYear fy " +
                "WHERE fy.legalEntity = ?1 ORDER BY start DESC ")
})
@Table(name = "fiscalyear")
public class FiscalYear extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "FiscalYear::findByLegalEntity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date start;

    @Column(nullable = false)
    private Date end;

    @Column(unique = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private FiscalYearStatus fiscalYearStatus = FiscalYearStatus.OPEN;

    @ManyToOne(optional = false)
    private LegalEntity legalEntity;

    @Transient
    String daysBetween;

    public FiscalYear() {
    }

    public FiscalYear(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public FiscalYearStatus getFiscalYearStatus() {
        return fiscalYearStatus;
    }

    public FiscalYear setFiscalYearStatus(FiscalYearStatus fiscalYearStatus) {
        this.fiscalYearStatus = fiscalYearStatus;
        return this;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public FiscalYear setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
        return this;
    }

    public String getDaysBetween() {
        return String.valueOf(Days.daysBetween(new DateTime(start), new DateTime(end)).getDays() + 1);
    }

    public void setDaysBetween(String daysBetween) {
        this.daysBetween = daysBetween;
    }

    public boolean isCurrentYear() {
        return (new Date()).after(start) && new Date().before(end);
    }

    public boolean isOpen() {
        return fiscalYearStatus == FiscalYearStatus.OPEN;
    }


}
