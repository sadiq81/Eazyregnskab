package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.ReportService;
import dk.eazyit.eazyregnskab.util.ReportObject;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class BalanceDataProvider extends EazyregnskabListDataProvider<FinanceAccount, ReportObject> {


    @SpringBean
    ReportService reportService;

    List<FinanceAccount> list;

    private static final Logger LOG = LoggerFactory.getLogger(BalanceDataProvider.class);

    public BalanceDataProvider(CompoundPropertyModel<ReportObject> model) {
        super(model);
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }

    @Override
    protected List<FinanceAccount> getData() {
        return list = reportService.getFinanceAccountsWithSum(getCurrentLegalEntity(), model);
    }

    @Override
    public void detach() {
        list = null;
    }
}
