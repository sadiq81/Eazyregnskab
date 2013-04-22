package dk.eazyit.eazyregnskab.web.components.navigation;

/**
 * @author Trifork
 */

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author literadix.de
 */
public class LabeledLink implements Serializable {

    static final Logger LOG = LoggerFactory.getLogger(LabeledLink.class);

    /**
     * Constant
     */
    private final static String linkObjectName = "link";

    /**
     * Constant
     */
    private final static String labelObjectName = "label";

    /**
     * Constant
     */
    private final static String beforeSelected = "";

    /**
     * Constant
     */
    private final static String afterSelected = "";

    /**
     * Linkobject.
     */
    private final Link link;

    /**
     * @return BookmarkablePageLink
     */
    public Link getLink() {
        return this.link;
    }

    /**
     * @param label
     * @param linkClass
     */
    public LabeledLink(final String label, Class linkClass) {
        LOG.trace("Changed legal entity selections");
        this.link = new BookmarkablePageLink(linkObjectName, linkClass);
        this.link.add(new Label(labelObjectName, new ResourceModel(label)));
        this.link.setAutoEnable(true);
        this.link.setBeforeDisabledLink(beforeSelected);
        this.link.setAfterDisabledLink(afterSelected);
    }
}
