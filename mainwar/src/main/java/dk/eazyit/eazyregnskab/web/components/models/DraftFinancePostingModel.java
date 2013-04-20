package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class DraftFinancePostingModel extends AbstractEntityModel<DraftFinancePosting> {

    @SpringBean
    DraftFinancePostingDAO draftFinancePostingDAO;

    public DraftFinancePostingModel(DraftFinancePosting entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public DraftFinancePostingModel(Class<? extends DraftFinancePosting> clazz, Long id) {
        super(DraftFinancePosting.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected DraftFinancePosting load(Long id) {
        if (id == 0) {
            return new DraftFinancePosting();
        } else {
            return draftFinancePostingDAO.findById(id);
        }
    }

    @Override
    public void setObject(DraftFinancePosting object) {
        if (object.getId() == 0) {
            draftFinancePostingDAO.create(object);
        } else {
            entity = draftFinancePostingDAO.save(object);
        }
    }
}
