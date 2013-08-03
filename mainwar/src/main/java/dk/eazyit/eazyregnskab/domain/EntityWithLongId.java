package dk.eazyit.eazyregnskab.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trifork
 */
public interface EntityWithLongId extends Serializable {

    Long getId();

    Date getCreated() ;

    void setCreated(Date created) ;

    Date getLastChanged() ;

    void setLastChanged(Date lastChanged) ;

}
