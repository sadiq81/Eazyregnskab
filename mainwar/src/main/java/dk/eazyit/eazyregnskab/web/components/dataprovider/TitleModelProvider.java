package dk.eazyit.eazyregnskab.web.components.dataprovider;

import org.apache.wicket.model.IModel;

/**
 * @author
 */
public interface TitleModelProvider<T> {

    public IModel<T> getTitleModel();
}
