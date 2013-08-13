package dk.eazyit.eazyregnskab.web.components.carousel;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.BootstrapResourcesBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;
import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.ICarouselImage;
import de.agilecoders.wicket.core.util.Attributes;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.time.Duration;

import java.util.List;

/**
 * @author
 */
public class LinkCarousel extends Panel {

    private Duration interval = Duration.seconds(5);
    private final IModel<List<LinkCarouselImage>> model;

    /**
     * Construct.
     *
     * @param markupId the component id
     * @param images   the list of images
     */
    public LinkCarousel(final String markupId, final List<LinkCarouselImage> images) {
        this(markupId, new ListModel(images));
    }

    /**
     * Construct.
     *
     * @param markupId the component id
     * @param model    the list of images
     */
    public LinkCarousel(final String markupId, final IModel<List<LinkCarouselImage>> model) {
        super(markupId, model);
        this.model = model;

        setOutputMarkupId(true);

        BootstrapResourcesBehavior.addTo(this);

        add(newImageList("images"),
                newNavigationButton("prev"),
                newNavigationButton("next"));
    }



    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        checkComponentTag(tag, "div");
        Attributes.addClass(tag, "carousel", "slide");
    }

    /**
     * creates a new navigation button
     *
     * @param markupId the component id
     * @return new navigation button
     */
    private Component newNavigationButton(final String markupId) {
        final Label button = new Label(markupId);
        button.add(new AttributeModifier("href", "#" + getMarkupId(true)));
        button.setEscapeModelStrings(false);

        if ("prev".equals(markupId)) {
            button.setDefaultModel(createPrevLabel());
        } else {
            button.setDefaultModel(createNextLabel());
        }

        return button;
    }

    /**
     * @return label of previous button
     */
    protected IModel<String> createPrevLabel() {
        return Model.of("&lsaquo;");
    }

    /**
     * @return label of next button
     */
    protected IModel<String> createNextLabel() {
        return Model.of("&rsaquo;");
    }

    /**
     * creates a new {@link ICarouselImage} list view
     *
     * @param wicketId The component id
     * @return new list view.
     */
    private Component newImageList(String wicketId) {
        return new ListView<LinkCarouselImage>(wicketId, model) {

            private boolean renderedActive = false;

            @Override
            protected void populateItem(ListItem<LinkCarouselImage> item) {
                final LinkCarouselImage carouselImage = item.getModelObject();

                final ExternalLink link = newLink("link", carouselImage);
                final Component image = newImage("image", carouselImage);
                final Component header = newHeader("header", carouselImage);
                final Component description = newDescription("description", carouselImage);

                final TransparentWebMarkupContainer caption = new TransparentWebMarkupContainer("caption");
                caption.setVisible(header.isVisible() || description.isVisible());

                // REFACTOR: use better way to detect first element
                if (!renderedActive) {
                    renderedActive = true;

                    item.add(new CssClassNameAppender("active"));
                }
                item.add(link.add(image, header, description, caption));
            }
        };
    }

    private ExternalLink newLink(String markupId, final LinkCarouselImage carouselImage) {
        final ExternalLink link = new ExternalLink(markupId, carouselImage.getLinkUrl());
        return link;
    }

    /**
     * creates a new image element
     *
     * @param markupId the markup id of the header
     * @param image    the current image for this header
     * @return new image component
     */
    protected Component newImage(final String markupId, final ICarouselImage image) {
        final Label img = new Label(markupId);
        img.add(new AttributeModifier("src", image.url()));

        return img;
    }

    /**
     * creates a new image description element
     *
     * @param markupId the markup id of the header
     * @param image    the current image for this header
     * @return new description component
     */
    protected Component newDescription(final String markupId, final LinkCarouselImage image) {
        Label description = new Label(markupId);
        if (image.description() != null) {
            description.setDefaultModel(new ResourceModel(image.description()));
        } else {
            description.setVisible(false);
        }

        return description;
    }

    /**
     * creates a new image header element
     *
     * @param markupId the markup id of the header
     * @param image    the current image for this header
     * @return new header component
     */
    protected Component newHeader(final String markupId, final LinkCarouselImage image) {
        final Label header = new Label(markupId);
        if (image.header() != null) {
            header.setDefaultModel(new ResourceModel(image.header()));
        } else {
            header.setVisible(false);
        }

        return header;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.render(OnDomReadyHeaderItem.forScript("$('#" + getMarkupId(true) + "').carousel({\n"
                + "  interval: " + getInterval().getMilliseconds() + "\n"
                + "})"));
    }

    /**
     * @return current interval as {@link Duration}
     */
    public final Duration getInterval() {
        return interval;
    }

    /**
     * The amount of time to delay between automatically cycling an item.
     * If Duration.NONE or value is 0, carousel will not automatically cycle.
     *
     * @param interval The duration
     * @return this instance for chaining
     */
    public final LinkCarousel setInterval(final Duration interval) {
        this.interval = interval;
        return this;
    }

}