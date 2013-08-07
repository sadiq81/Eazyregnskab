package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.ReportService;
import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class BookedFinancePostingListModelWithSum extends AbstractEntityListModel<FinanceAccount, LegalEntity> {

    @SpringBean
    ReportService reportService;

    CompoundPropertyModel<ReportObject> cpm;

    static final Logger LOG = LoggerFactory.getLogger(BookedFinancePostingListModelWithSum.class);

    public BookedFinancePostingListModelWithSum(CompoundPropertyModel<ReportObject> cpm) {
        Injector.get().inject(this);
        this.cpm = cpm;
    }

    public BookedFinancePostingListModelWithSum(LegalEntity parent, CompoundPropertyModel<ReportObject> cpm) {
        super(parent);
        Injector.get().inject(this);
        this.cpm = cpm;
    }

    public BookedFinancePostingListModelWithSum(LegalEntity parent, List<FinanceAccount> list, CompoundPropertyModel<ReportObject> cpm) {
        super(parent, list);
        Injector.get().inject(this);
        this.cpm = cpm;
    }

    @Override
    protected List<FinanceAccount> load(LegalEntity id) {
        return reportService.getFinanceAccountsWithBookedFinancePostings(id, cpm);
    }

    @Override
    protected LegalEntity fetchParent() {
        return getCurrentLegalEntity();
    }

    @Override
    public void setObject(List<FinanceAccount> object) {
        for (FinanceAccount financeAccount : object) {
            //TODO
        }
    }

}
