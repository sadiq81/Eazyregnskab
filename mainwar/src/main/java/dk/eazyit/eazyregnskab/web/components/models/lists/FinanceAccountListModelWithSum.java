package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.ReportService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class FinanceAccountListModelWithSum extends AbstractEntityListModel<FinanceAccount, LegalEntity> {

    @SpringBean
    ReportService reportService;

    static final Logger LOG = LoggerFactory.getLogger(FinanceAccountListModelWithSum.class);

    public FinanceAccountListModelWithSum() {
        Injector.get().inject(this);
    }

    public FinanceAccountListModelWithSum(LegalEntity parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public FinanceAccountListModelWithSum(LegalEntity parent, List<FinanceAccount> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<FinanceAccount> load(LegalEntity id) {
        return reportService.getFinanceAccountsWithSum(id);
    }

    @Override
    protected LegalEntity fetchParent() {
        return getSelectedLegalEntity().getLegalEntityModel().getObject();
    }

    @Override
    public void setObject(List<FinanceAccount> object) {
        for (FinanceAccount financeAccount : object) {
            //TODO
        }
    }

}
