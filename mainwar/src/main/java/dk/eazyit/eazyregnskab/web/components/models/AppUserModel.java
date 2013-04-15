package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.AppUserDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class AppUserModel extends AbstractEntityModel<AppUser> {

    @SpringBean
    AppUserDAO appUserDAO;

    public AppUserModel(AppUser entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public AppUserModel(Class<? extends AppUser> clazz, Long id) {
        super(AppUser.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected AppUser load(Long id) {
        if (id == 0) {
            return new AppUser();
        } else {
            return appUserDAO.findById(id);
        }
    }

    @Override
    public void setObject(AppUser object) {
        if (object.getId() == 0) {
            appUserDAO.create(object);
        } else {
            entity = appUserDAO.save(object);
        }
    }
}
