package dk.eazyit.eazyregnskab.web.components.carousel;

import de.agilecoders.wicket.core.markup.html.bootstrap.carousel.ICarouselImage;

import java.io.Serializable;

/**
 * @author
 */
public class LinkCarouselImage implements ICarouselImage, Serializable{

    private final String header;
    private final String description;
    private final String imageUrl;
    private final String linkUrl;

    public LinkCarouselImage(String imageUrl, String linkUrl) {
        this(null, null, imageUrl, linkUrl);
    }

    public LinkCarouselImage(String header, String description, String imageUrl, String linkUrl) {
        this.header = header;
        this.description = description;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
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
