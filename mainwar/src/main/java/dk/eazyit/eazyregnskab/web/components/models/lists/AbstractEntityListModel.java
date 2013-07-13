package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eazy It
 */
public abstract class AbstractEntityListModel<T extends EntityWithLongId, E extends EntityWithLongId> implements IModel<List<T>> {

    protected E parent = null;
    protected List<T> list = null;

    protected AbstractEntityListModel() {
    }

    public AbstractEntityListModel(E parent) {
        this.parent = parent;
    }

    public AbstractEntityListModel(E parent, List<T> list) {
        this.parent = parent;
        this.list = list;
    }

    public List<T> getObject() {
        if (list == null) {
            if (parent == null) {
                list = load(parent = fetchParent());
            } else {
                list = load(parent);
            }
            if (list == null) {
                return new ArrayList<T>();
            }
        }
        return list;
    }


    public void detach() {
        if (list != null) {
            if (parent != null) {
                parent = null;
            }
            list = null;
        }
    }

    protected abstract List<T> load(E id);

    protected abstract E fetchParent();

    public abstract void setObject(List<T> object);

    protected AppUser getCurrentUser() {
        return (AppUser) Session.get().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    protected LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) Session.get().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    protected void setCurrentLegalEntity(LegalEntity legalEntity) {
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
    }

    protected DailyLedger getCurrentDailyLedger() {
        return (DailyLedger) Session.get().getAttribute(DailyLedger.ATTRIBUTE_NAME);
    }

    protected void setCurrentDailyLedger(DailyLedger dailyLedger) {
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }

}