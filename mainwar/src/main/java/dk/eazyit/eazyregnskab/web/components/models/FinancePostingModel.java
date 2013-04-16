package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.FinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.FinancePosting;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class FinancePostingModel extends AbstractEntityModel<FinancePosting> {

    @SpringBean
    FinancePostingDAO financePostingDAO;

    public FinancePostingModel(FinancePosting entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public FinancePostingModel(Class<? extends FinancePosting> clazz, Long id) {
        super(FinancePosting.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected FinancePosting load(Long id) {
        if (id == 0) {
            return new FinancePosting();
        } else {
            return financePostingDAO.findById(id);
        }
    }

    @Override
    public void setObject(FinancePosting object) {
        if (object.getId() == 0) {
            financePostingDAO.create(object);
        } else {
            entity = financePostingDAO.save(object);
        }
    }
}
