package dk.eazyit.eazyregnskab.web.components.navigation;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author Trifork
 */
public class LabeledLinkList extends ListView<LabeledLink> {

    public LabeledLinkList(String id) {
        super(id);
        setRenderBodyOnly(true);
    }

    public LabeledLinkList(String id, IModel model) {
        super(id, model);
        setRenderBodyOnly(true);
    }

    public LabeledLinkList(String id, List list) {
        super(id, list);
        setRenderBodyOnly(true);
    }

    @Override
    protected void populateItem(ListItem<LabeledLink> listItem) {
        final LabeledLink link = listItem.getModelObject();
        Link wicketLink = link.getLink();
        listItem.setRenderBodyOnly(true);
        listItem.add(wicketLink);
    }
}


