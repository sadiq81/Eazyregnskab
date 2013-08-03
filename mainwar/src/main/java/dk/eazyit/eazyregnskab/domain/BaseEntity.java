package dk.eazyit.eazyregnskab.domain;

import java.util.Date;

/**
 * @author Trifork
 */
public abstract class BaseEntity  implements EntityWithLongId {

    protected Date created;
    protected Date lastChanged;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getId() == null) {
            return false;
        } else if (obj instanceof EntityWithLongId) {
            EntityWithLongId otherIdentifiable = (EntityWithLongId) obj;
            return getId().equals(otherIdentifiable.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this.lastChanged = lastChanged;
    }
}
