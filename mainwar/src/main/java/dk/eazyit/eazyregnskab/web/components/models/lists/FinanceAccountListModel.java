package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class FinanceAccountListModel extends AbstractEntityListModel<FinanceAccount, LegalEntity> {

    @SpringBean
    FinanceAccountService financeAccountService;

    static final Logger LOG = LoggerFactory.getLogger(FinanceAccountListModel.class);

    public FinanceAccountListModel() {
        Injector.get().inject(this);
    }

    public FinanceAccountListModel(LegalEntity parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public FinanceAccountListModel(LegalEntity parent, List<FinanceAccount> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<FinanceAccount> load(LegalEntity id) {
        return financeAccountService.findFinanceAccountByLegalEntity(id);
    }

    @Override
    protected LegalEntity fetchParent() {
        return getCurrentLegalEntity();
    }

    @Override
    public void setObject(List<FinanceAccount> object) {
        for (FinanceAccount financeAccount : object) {
            financeAccountService.saveFinanceAccount(financeAccount, fetchParent());
        }
    }

}
