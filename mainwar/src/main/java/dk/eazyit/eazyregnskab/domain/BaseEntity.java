package dk.eazyit.eazyregnskab.domain;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author Eazy IT
 */
@MappedSuperclass
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity  implements EntityWithLongId {

    @Column(nullable = false)
    protected Date created;
    @Column(nullable = false)
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
