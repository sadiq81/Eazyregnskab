package dk.eazyit.eazyregnskab.web.components.dataprovider.test;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.ReportService;
import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.dataprovider.EazyregnskabSortableDataProvider;
import dk.eazyit.eazyregnskab.web.components.models.entities.FinanceAccountModel;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author
 */
public class FinanceAccountTestDataProvider extends EazyregnskabSortableDataProvider<FinanceAccount> {

    CompoundPropertyModel<ReportObject> model;

    @SpringBean
    ReportService reportService;

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountTestDataProvider.class);

    public FinanceAccountTestDataProvider(CompoundPropertyModel<ReportObject> model) {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
        this.model = model;
    }

    @Override
    public Iterator<FinanceAccount> iterator(long first, long count) {
        return reportService.getFinanceAccountsWithBookedFinancePostings(getCurrentLegalEntity(), model).iterator();
    }

    @Override
    public long size() {
        return reportService.getFinanceAccountsWithBookedFinancePostings(getCurrentLegalEntity(), model).size();
    }

    @Override
    public IModel<FinanceAccount> model(FinanceAccount object) {
        return new FinanceAccountModel(object);
    }
}
