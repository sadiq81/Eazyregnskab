package dk.eazyit.eazyregnskab.web.components.models;

import java.io.Serializable;

/**
 * @author Trifork
 */
public interface Identifiable<T extends Serializable>  {

    T getId();
}
