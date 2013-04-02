package dk.eazyit.eazyregnskab.domain;

/**
 * @author Trifork
 */
public abstract class BaseEntity implements EntityWithLongId {

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
}
