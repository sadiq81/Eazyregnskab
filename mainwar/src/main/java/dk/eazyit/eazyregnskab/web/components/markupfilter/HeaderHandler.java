package dk.eazyit.eazyregnskab.web.components.markupfilter;

import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;

import java.text.ParseException;

/**
 * @author
 */
public class HeaderHandler extends AbstractMarkupFilter{

    @Override
    protected MarkupElement onComponentTag(ComponentTag tag) throws ParseException {

        if (tag.getXmlTag().getName().equals("html")){
            tag.put("lang", Session.get().getLocale().toString());
        }
        return tag;
    }
}
