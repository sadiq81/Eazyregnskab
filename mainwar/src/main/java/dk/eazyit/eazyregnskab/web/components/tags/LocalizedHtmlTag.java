package dk.eazyit.eazyregnskab.web.components.tags;

/**
 * @author
 */

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class LocalizedHtmlTag extends WebMarkupContainer {

     public LocalizedHtmlTag(String id) {
         super(id);
         String language = getSession().getLocale().getLanguage();
         add(new AttributeModifier("lang", language));
         add(new AttributeModifier("xml:lang", language));
     }
}

