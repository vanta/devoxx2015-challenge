package pl.allegro.promo.devoxx2015.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferPublishedEvent {

    private final String id;
    private final String title;
    private final String photoUrl;

    @JsonCreator
    public OfferPublishedEvent(@JsonProperty("id") String id,
                               @JsonProperty("title") String title,
                               @JsonProperty("photoUrl") String photoUrl) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

}
