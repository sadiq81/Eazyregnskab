package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.services.FiscalYearService;
import dk.eazyit.eazyregnskab.web.components.models.entities.FiscalYearModel;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author
 */
public class FiscalYearDataProvider extends EazyregnskabSortableDataProvider<FiscalYear> {

    @SpringBean
    FiscalYearService fiscalYearService;

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearDataProvider.class);

    public FiscalYearDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<FiscalYear> iterator(long first, long count) {

        LOG.debug("Creating iterator of FiscalYear with first " + first + " and count " + count);
        List<FiscalYear> list = fiscalYearService.findFiscalYearByLegalEntitySubList(getCurrentLegalEntity(), (int) first, (int) count);
        return list.iterator();
    }

    @Override
    public long size() {
        int size = fiscalYearService.findFiscalYearByLegalEntity(getCurrentLegalEntity()).size();
        return size;
    }

    @Override
    public IModel<FiscalYear> model(FiscalYear object) {
        return new FiscalYearModel(object);
    }
}
