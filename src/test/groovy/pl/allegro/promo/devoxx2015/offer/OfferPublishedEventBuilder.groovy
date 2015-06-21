package pl.allegro.promo.devoxx2015.offer

import pl.allegro.promo.devoxx2015.application.OfferPublishedEvent

class OfferPublishedEventBuilder {

    private String id = "1"
    private String title = "Jet propelled unicycle"
    private String photoUrl = "http://acme.com/catalog/acmejetuni.jpg"

    private OfferPublishedEventBuilder() {
    }

    static OfferPublishedEventBuilder offerPublishedEvent() {
        return new OfferPublishedEventBuilder()
    }

    OfferPublishedEventBuilder id(String id) {
        this.id = id
        this
    }

    OfferPublishedEventBuilder title(String title) {
        this.title = title
        this
    }

    OfferPublishedEventBuilder photoUrl(String photoUrl) {
        this.photoUrl = photoUrl
        this
    }

    OfferPublishedEvent build() {
        return new OfferPublishedEvent(id, title, photoUrl)
    }

}
