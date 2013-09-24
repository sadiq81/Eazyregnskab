package dk.eazyit.eazyregnskab.web.components.carousel;

import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.ICarouselImage;
import dk.eazyit.eazyregnskab.web.components.button.LoadingButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class LinkCarouselImage implements ICarouselImage, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(LoadingButton.class);

    private final String header;
    private final String description;
    private final String imageUrl;
    private final String linkUrl;

    public LinkCarouselImage(String imageUrl, String linkUrl) {
        this(null, null, imageUrl, linkUrl);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with image " + imageUrl);
    }

    public LinkCarouselImage(String header, String description, String imageUrl, String linkUrl) {
        this.header = header;
        this.description = description;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        LOG.trace("creating " + this.getClass().getSimpleName() + " with image " + imageUrl);
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    @Override
    public String url() {
        return imageUrl;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String header() {
        return header;
    }
}
