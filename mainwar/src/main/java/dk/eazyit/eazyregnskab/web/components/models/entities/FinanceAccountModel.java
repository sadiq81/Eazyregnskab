package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class FinanceAccountModel extends AbstractEntityModel<FinanceAccount> {

    @SpringBean
    FinanceAccountDAO financeAccountDAO;

    static final Logger LOG = LoggerFactory.getLogger(FinanceAccountModel.class);

    public FinanceAccountModel(FinanceAccount entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created FinanceAccountModel with id " + entity.getId());
    }

    public FinanceAccountModel(Class<? extends FinanceAccount> clazz, Long id) {
        super(FinanceAccount.class, id);
        Injector.get().inject(this);
        LOG.trace("Created FinanceAccountModel with id " + entity.getId());
    }

    @Override
    protected FinanceAccount load(Long id) {
        if (id == 0) {
            if (entity == null) {
                LOG.trace("loading empty FinanceAccount entity ");
                return entity = new FinanceAccount();
            } else {
                return entity;
            }
        } else {
            if (entity == null) {
                LOG.trace("loading FinanceAccount entity " + id);
                return financeAccountDAO.findById(id);
            } else {
                return entity;
            }
        }
    }

    @Override
    public void setObject(FinanceAccount object) {
        LOG.trace("setting FinanceAccount entity " + object.getId());
        if (object.getId() == 0) {
            financeAccountDAO.create(object);
            entity = object;
        } else {
            entity = financeAccountDAO.save(object);
        }
    }
}
