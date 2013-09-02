package dk.eazyit.eazyregnskab.domain;

import com.google.common.base.Objects;
import com.pdfjet.A4;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

import javax.persistence.*;
import java.util.Set;

/**
 * @author
 */
@Entity
@NamedQueries({
        @NamedQuery(name = DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, query = "select dl from DailyLedger dl where dl.legalEntity= ?1 "),
        @NamedQuery(name = DailyLedger.QUERY_FIND_BY_NAME_AND_LEGAL_ENTITY, query = "select dl from DailyLedger dl where dl.name =?1 AND dl.legalEntity= ?2 ")
})
@Table(name = "dailyledger")
public class DailyLedger extends BaseEntity implements ExportTableRow<DailyLedger> {

    public static final String QUERY_FIND_BY_LEGAL_ENTITY = "DailyLedger::findByLegalEntity";
    public static final String QUERY_FIND_BY_NAME_AND_LEGAL_ENTITY = "DailyLedger::findByNameAndLegalEntity";

    public static final String ATTRIBUTE_NAME = DailyLedger.class.getName();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToOne(optional = false)
    private LegalEntity legalEntity;

    @Column(nullable = true)
    private int nextBookingNumber;

    @OneToMany(mappedBy = "dailyLedger", fetch = FetchType.LAZY)
    private Set<DraftFinancePosting> draftFinancePosting;

    @ManyToOne(optional = true)
    private FinanceAccount financeAccount;

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

    public int getNextBookingNumber() {
        return nextBookingNumber;
    }

    public void setNextBookingNumber(int nextBookingNumber) {
        this.nextBookingNumber = nextBookingNumber;
    }

    public Set<DraftFinancePosting> getDraftFinancePosting() {
        return draftFinancePosting;
    }

    public void setDraftFinancePosting(Set<DraftFinancePosting> draftFinancePosting) {
        this.draftFinancePosting = draftFinancePosting;
    }

    public FinanceAccount getFinanceAccount() {
        return financeAccount;
    }

    public void setFinanceAccount(FinanceAccount financeAccount) {
        this.financeAccount = financeAccount;
    }

    @Override
    public String getCssClassForDataTable() {
        return "";
    }

    @Override
    public Font getFont(PDF pdf) throws Exception {
        Font normalFont = new Font(pdf, CoreFont.TIMES_ROMAN);
        normalFont.setSize(8F);
        return normalFont;
    }

    @Override
    public WritableCellFormat getCellFormat() {
        return new WritableCellFormat(new WritableFont(WritableFont.TIMES, 8));
    }

    @Override
    public boolean insertSpaceAfterRowInTables() {
        return false;
    }


    @Override
    public float[] getPageSize() {
        return A4.PORTRAIT;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
//                .add("legalEntity", legalEntity)
//                .add("draftFinancePosting", draftFinancePosting)
                .toString();
    }

}
