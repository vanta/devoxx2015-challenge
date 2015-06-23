package pl.allegro.promo.devoxx2015.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.allegro.promo.devoxx2015.domain.Offer;
import pl.allegro.promo.devoxx2015.domain.OfferRepository;
import pl.allegro.promo.devoxx2015.domain.PhotoScoreSource;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
public class OfferService {

    private static final double MINIMAL_PRETTY_PHOTO_SCORE = 0.7;

    private static final Sort PHOTO_SCORE_SORT_DESC = new Sort(DESC, "photoScore");

    private final OfferRepository offerRepository;
    private final PhotoScoreSource photoScoreSource;

    @Autowired
    public OfferService(OfferRepository offerRepository, PhotoScoreSource photoScoreSource) {
        this.offerRepository = offerRepository;
        this.photoScoreSource = photoScoreSource;
    }

    public void processOffers(List<OfferPublishedEvent> events) {
        final List<Offer> offers = events.stream()
                .map(this::createOffer)
                .filter(offer -> offer.getPhotoScore() >= MINIMAL_PRETTY_PHOTO_SCORE)
                .collect(toList());

        offerRepository.save(offers);
    }

    private Offer createOffer(OfferPublishedEvent ope) {
        return new Offer(ope.getId(), ope.getTitle(), ope.getPhotoUrl(), getScoreForPhoto(ope.getPhotoUrl()));
    }

    private double getScoreForPhoto(String photoUrl) {
        try {
            return photoScoreSource.getScore(photoUrl);
        } catch (Exception e) {
            return MINIMAL_PRETTY_PHOTO_SCORE;
        }
    }

    public List<Offer> getOffers() {
        return offerRepository.findAll(PHOTO_SCORE_SORT_DESC);
    }

}
