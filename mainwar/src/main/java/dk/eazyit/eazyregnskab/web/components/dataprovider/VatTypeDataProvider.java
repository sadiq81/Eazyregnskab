package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.VatTypeModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 */
public class VatTypeDataProvider extends EazyregnskabSortableDataProvider<VatType> {

    @SpringBean
    FinanceAccountService financeAccountService;

    public VatTypeDataProvider() {
        Injector.get().inject(this);
    }


    @Override
    public Iterator<VatType> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
            List<VatType> list = financeAccountService.findVatTypeByLegalEntitySubList(legalEntity, (int) first, (int) count);
            Iterator<VatType> iterator = list.iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
        int size = financeAccountService.countVatTypesOfLegalEntity(legalEntity);
        return size;
    }

    @Override
    public IModel<VatType> model(VatType object) {
        return new VatTypeModel(object);
    }
}
