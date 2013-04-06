package dk.eazyit.eazyregnskab.web.components.navigation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * Navigation link set.
 *
 * @author literadix.de
 */
public class MenuPanel extends Panel {

    /**
     * @param id
     * @param label
     * @param subMenues
     */
    public MenuPanel(String id, String label, List<LabeledLink> subMenues) {
        super(id);

        this.setRenderBodyOnly(true);

        Label topicLabel = new Label("panelLabel", label);
        topicLabel.setRenderBodyOnly(true);
        add(topicLabel);

        ListView listView = new ListView("list", subMenues) {

            @Override
            protected void populateItem(ListItem listItem) {
                final LabeledLink link = (LabeledLink) listItem.getModelObject();
                Link wicketLink = link.getLink();
                listItem.setRenderBodyOnly(true);
                listItem.add(wicketLink);
            }
        };

        listView.setRenderBodyOnly(true);

        add(listView);
    }
}
