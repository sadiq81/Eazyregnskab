package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class LegalEntityModel extends AbstractEntityModel<LegalEntity> {

    @SpringBean
    LegalEntityDAO legalEntityDAO;

    static final Logger LOG = LoggerFactory.getLogger(LegalEntityModel.class);

    public LegalEntityModel(LegalEntity entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created LegalEntityModel with id " + entity.getId());
    }

    public LegalEntityModel(Class<? extends LegalEntity> clazz, Long id) {
        super(LegalEntity.class, id);
        Injector.get().inject(this);
        LOG.trace("Created LegalEntityModel with id " + entity.getId());
    }

    @Override
    protected LegalEntity load(Long id) {
        if (id == 0) {
            if (entity != null) LOG.trace("loading empty LegalEntity entity ");
            return new LegalEntity();
        } else {
            if (entity != null) LOG.trace("loading LegalEntity entity " + entity.getId());
            return legalEntityDAO.findById(id);
        }
    }

    @Override
    public void setObject(LegalEntity object) {
        LOG.trace("setting LegalEntity entity " + object.getId());
        if (object.getId() == 0) {
            legalEntityDAO.create(object);
        } else {
            entity = legalEntityDAO.save(object);
        }
    }
}
