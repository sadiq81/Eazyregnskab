package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.VatTypeService;
import dk.eazyit.eazyregnskab.web.components.models.entities.VatTypeModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
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
public class VatTypeDataProvider extends EazyregnskabSortableDataProvider<VatType> {

    @SpringBean
    VatTypeService vatTypeService;

    private static final Logger LOG = LoggerFactory.getLogger(VatTypeDataProvider.class);

    public VatTypeDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<VatType> iterator(long first, long count) {

        LOG.debug("Creating iterator of VatType with first " + first + " and count " + count);
        SortParam sortParam = getSort();
        if (sortParam != null) {
            LegalEntity legalEntity = getCurrentLegalEntity();
            List<VatType> list = vatTypeService.findVatTypeByLegalEntitySubListSortBy(legalEntity, (int) first, (int) count, sortParam.getProperty().toString(), sortParam.isAscending());
            Iterator<VatType> iterator = list.iterator();
            return iterator;
        } else {
            LegalEntity legalEntity = getCurrentLegalEntity();
            List<VatType> list = vatTypeService.findVatTypeByLegalEntitySubList(legalEntity, (int) first, (int) count);
            Iterator<VatType> iterator = list.iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        LegalEntity legalEntity = getCurrentLegalEntity();
        int size = vatTypeService.countVatTypesOfLegalEntity(legalEntity);
        return size;
    }

    @Override
    public IModel<VatType> model(VatType object) {
        return new VatTypeModel(object);
    }
}
