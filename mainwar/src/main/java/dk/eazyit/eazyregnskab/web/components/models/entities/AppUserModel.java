package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class AppUserModel extends AbstractEntityModel<AppUser> {

    @SpringBean
    AppUserDAO appUserDAO;

    static final Logger LOG = LoggerFactory.getLogger(AppUserModel.class);

    public AppUserModel(AppUser entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created AppUserModel with id" + entity.getId());
    }

    public AppUserModel(Class<? extends AppUser> clazz, Long id) {
        super(AppUser.class, id);
        Injector.get().inject(this);
        LOG.trace("Created AppUserModel with id" + entity.getId());
    }

    @Override
    protected AppUser load(Long id) {
        if (id == 0) {
            if (entity != null)  LOG.trace("loading empty appuser entity");
            return new AppUser();
        } else {
            if (entity != null)   LOG.trace("loading appuser entity" + entity.getId());
            return appUserDAO.findById(id);
        }
    }

    @Override
    public void setObject(AppUser object) {
        LOG.trace("setting appuser entity " + object.getId());
        if (object.getId() == 0) {
            appUserDAO.create(object);
        } else {
            entity = appUserDAO.save(object);
        }
    }
}
