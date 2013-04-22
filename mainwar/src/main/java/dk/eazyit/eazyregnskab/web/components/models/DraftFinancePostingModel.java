package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class DraftFinancePostingModel extends AbstractEntityModel<DraftFinancePosting> {

    @SpringBean
    DraftFinancePostingDAO draftFinancePostingDAO;

    static final Logger LOG = LoggerFactory.getLogger(DraftFinancePostingModel.class);

    public DraftFinancePostingModel(DraftFinancePosting entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created DraftFinancePostingModel with id " + entity.getId());
    }

    public DraftFinancePostingModel(Class<? extends DraftFinancePosting> clazz, Long id) {
        super(DraftFinancePosting.class, id);
        Injector.get().inject(this);
        LOG.trace("Created DraftFinancePostingModel with id " + entity.getId());
    }

    @Override
    protected DraftFinancePosting load(Long id) {
        if (id == 0) {
            if (entity != null)  LOG.trace("loading empty DraftFinancePosting entity ");
            return new DraftFinancePosting();
        } else {
            if (entity != null)   LOG.trace("loading DraftFinancePosting entity " + entity.getId());
            return draftFinancePostingDAO.findById(id);
        }
    }

    @Override
    public void setObject(DraftFinancePosting object) {
        LOG.trace("setting DraftFinancePosting entity " + object.getId());
        if (object.getId() == 0) {
            draftFinancePostingDAO.create(object);
        } else {
            entity = draftFinancePostingDAO.save(object);
        }
    }
}
