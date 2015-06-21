package pl.allegro.promo.devoxx2015.application.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.allegro.promo.devoxx2015.application.OfferPublishedEvent;
import pl.allegro.promo.devoxx2015.application.OfferService;
import pl.allegro.promo.devoxx2015.domain.Offer;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void newOffer(@RequestBody List<OfferPublishedEvent> offerPublicationEvents) {
        offerService.processOffers(offerPublicationEvents);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Offer> getOffers() {
        return offerService.getOffers();
    }

}
