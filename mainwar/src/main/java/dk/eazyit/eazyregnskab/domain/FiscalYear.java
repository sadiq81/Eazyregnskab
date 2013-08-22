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
                "WHERE fy.legalEntity = ?1 ORDER BY start DESC "),

        @NamedQuery(name = FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY, query = "select fy from FiscalYear fy " +
                "WHERE fy.legalEntity = ?1 and fy.fiscalYearStatus =?2 ORDER BY start DESC "),

        @NamedQuery(name = FiscalYear.QUERY_FIND_NEXT_OPEN_BY_LEGAL_ENTITY, query = "select fy from FiscalYear fy " +
                "WHERE fy.legalEntity = ?1 and fy.start = ?2 and fy.fiscalYearStatus =?3 "),

        @NamedQuery(name = FiscalYear.QUERY_FIND_LOCKED_BY_LEGAL_ENTITY_AFTER_DATE, query = "select fy from FiscalYear fy " +
                "WHERE fy.legalEntity = ?1 and fy.start > ?2 and fy.fiscalYearStatus = ?3"),

        @NamedQuery(name = FiscalYear.QUERY_FIND_OPEN_BY_LEGAL_ENTITY_BEFORE_DATE, query = "select fy from FiscalYear fy " +
                "WHERE fy.legalEntity = ?1 and fy.end < ?2 and fy.fiscalYearStatus = ?3"),

        @NamedQuery(name = FiscalYear.QUERY_FIND_LAST_BY_LEGAL_ENTITY, query = "select fy from FiscalYear fy " +
                "WHERE fy.start = (select MAX(fy2.start) from FiscalYear fy2 where fy2.legalEntity = ?1 ) and fy.legalEntity = ?1"),


})
@Table(name = "fiscalyear")
public class FiscalYear extends BaseEntity {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "FiscalYear::findByLegalEntity";
    public static final String QUERY_FIND_OPEN_BY_LEGAL_ENTITY = "FiscalYear::findOpenByLegalEntity";
    public static final String QUERY_FIND_NEXT_OPEN_BY_LEGAL_ENTITY = "FiscalYear::findNextOpenByLegalEntity";
    public static final String QUERY_FIND_LOCKED_BY_LEGAL_ENTITY_AFTER_DATE = "FiscalYear::findByLegalEntityAfterDate";
    public static final String QUERY_FIND_OPEN_BY_LEGAL_ENTITY_BEFORE_DATE = "FiscalYear::findByLegalEntityBeforeDate";
    public static final String QUERY_FIND_LAST_BY_LEGAL_ENTITY = "FiscalYear::findLastByLegalEntity";

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

    public FiscalYear(Date start, Date end, LegalEntity legalEntity) {
        this.start = start;
        this.end = end;
        this.legalEntity = legalEntity;
        this.fiscalYearStatus = FiscalYearStatus.OPEN;
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
