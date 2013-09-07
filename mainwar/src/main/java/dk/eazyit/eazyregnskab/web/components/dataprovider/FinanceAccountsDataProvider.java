package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
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
public class FinanceAccountsDataProvider extends EazyregnskabListDataProvider<BookedFinancePosting, ReportObject> {


    @SpringBean
    ReportService reportService;

    List<BookedFinancePosting> list;

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountsDataProvider.class);

    public FinanceAccountsDataProvider(CompoundPropertyModel<ReportObject> model) {
        super(model);
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }

    @Override
    protected List<BookedFinancePosting> getData() {
        if (list == null) list = reportService.getBookedFinancePostingsWithSum(getCurrentLegalEntity(), model);
        return list;
    }

    @Override
    public void detach() {
        list = null;
    }
}
