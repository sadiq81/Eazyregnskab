package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class FinanceAccountModel extends AbstractEntityModel<FinanceAccount> {

    @SpringBean
    FinanceAccountDAO financeAccountDAO;

    public FinanceAccountModel(FinanceAccount entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public FinanceAccountModel(Class<? extends FinanceAccount> clazz, Long id) {
        super(FinanceAccount.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected FinanceAccount load(Long id) {
        return financeAccountDAO.findById(id);
    }

    @Override
    public void setObject(FinanceAccount object) {
        if (object.getId() == 0) {
            financeAccountDAO.create(object);
        } else {
            entity = financeAccountDAO.save(object);
        }
    }
}
