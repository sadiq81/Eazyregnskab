package dk.eazyit.eazyregnskab.web.components.navigation;

import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author EazyIT
 */
public class LinkList extends RepeatingView {

    List<Class<? extends LoggedInPage>> list;

    public LinkList(String id, List<Class<? extends LoggedInPage>> list) {
        super(id);
        this.list = list;
        populate();
    }


    private void populate() {
        for (Class<? extends LoggedInPage> clazz : list) {
            MenuPosition menuPosition = (MenuPosition) clazz.getAnnotation(MenuPosition.class);
            WebMarkupContainer webMarkupContainer = new WebMarkupContainer(this.newChildId());
            BookmarkablePageLink bookmarkablePageLink = new BookmarkablePageLink("link", clazz);
            webMarkupContainer.add(bookmarkablePageLink);
            Label label = new Label("label", new ResourceModel(menuPosition.name()));
            bookmarkablePageLink.add(label);
            this.add(webMarkupContainer);
        }
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        super.onComponentTagBody(markupStream, openTag);
    }
}


