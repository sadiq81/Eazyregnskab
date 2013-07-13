package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class VatTypeListModel extends AbstractEntityListModel<VatType, LegalEntity> {

    @SpringBean
    FinanceAccountService financeAccountService;

    static final Logger LOG = LoggerFactory.getLogger(VatTypeListModel.class);

    public VatTypeListModel() {
        Injector.get().inject(this);
    }

    public VatTypeListModel(LegalEntity parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public VatTypeListModel(LegalEntity parent, List<VatType> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<VatType> load(LegalEntity id) {
        return financeAccountService.findAllVatTypesForLegalEntity(id);
    }

    @Override
    protected LegalEntity fetchParent() {
        return getCurrentLegalEntity();
    }

    @Override
    public void setObject(List<VatType> object) {
        for (VatType vatType : object) {
            financeAccountService.saveVatType(vatType, fetchParent());
        }
    }
}
