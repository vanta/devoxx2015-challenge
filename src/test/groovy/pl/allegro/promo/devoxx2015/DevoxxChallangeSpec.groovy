package pl.allegro.promo.devoxx2015

import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import pl.allegro.promo.devoxx2015.application.OfferPublishedEvent
import pl.allegro.promo.devoxx2015.domain.OfferRepository
import pl.allegro.promo.devoxx2015.report.TravisReporter
import spock.lang.Specification

import static pl.allegro.promo.devoxx2015.offer.OfferPublishedEventBuilder.offerPublishedEvent
import static pl.allegro.promo.devoxx2015.stub.PhotoScoreServiceStub.photoScoreServiceStub

@ContextConfiguration(loader = SpringApplicationContextLoader, classes = ACMEMostPretty)
@WebIntegrationTest
class DevoxxChallangeSpec extends Specification {

    static final String OFFERS_RESOURCE = "http://localhost:8080/offers"

    @Rule
    WireMockRule photoScoreService = new WireMockRule(1337)

    @Autowired
    OfferRepository offerRepository

    TestRestTemplate template = new TestRestTemplate()

    void cleanup() {
        offerRepository.deleteAll()
    }

    void cleanupSpec() {
        new TravisReporter().report()
    }

    def "should save offer with pretty photo"() {
        given:
        def offerWithPrettyPhoto = offerPublishedEvent().title("The Earthquake Pills").photoUrl("http://acme.com/catalog/earth.jpg").build()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithPrettyPhoto.photoUrl).returns(0.88).teach()

        when:
        sendOffers([offerWithPrettyPhoto])

        then:
        getOffers()[0].id == "1"
    }

    def "should not save offer with ugly photo"() {
        given:
        def offerWithUglyPhoto = offerPublishedEvent().title("DIY Tornado Kit").photoUrl("http://acme.com/catalog/doit.jpg").build()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithUglyPhoto.photoUrl).returns(0.21).teach()

        when:
        sendOffers([offerWithUglyPhoto])

        then:
        getOffers().isEmpty()
    }

    def "should not save anything on empty offers"() {
        when:
        sendOffers([])

        then:
        getOffers().isEmpty()
    }

    def "should not save offer with same id again when received for the second time"() {
        given:
        def offerWithPrettyPhoto = offerPublishedEvent().title("Atom Re-arranger").photoUrl("http://acme.com/catalog/atom.jpg").build()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithPrettyPhoto.photoUrl).returns(0.88).teach()

        when:
        2.times { sendOffers([offerWithPrettyPhoto]) }

        then:
        getOffers().size() == 1
    }

    def "should save offer regardless of photo prettiness with minimal acceptable score if photo score service was not available"() {
        given:
        def offerWithUglyPhoto = offerPublishedEvent().title("DIY Tornado Kit").photoUrl("http://acme.com/catalog/doit.jpg").build()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithUglyPhoto.photoUrl).fails().teach()

        when:
        sendOffers([offerWithUglyPhoto])

        then:
        getOffers()*.photoScore == [0.7]
    }

    def "should return offers sorted by photo score (descending)"() {
        given:
        def offerWithPrettyPhoto = offerPublishedEvent().id("1").title("Hitch-hiker's thumb").photoUrl("http://acme.com/catalog/hitch.jpg").build()
        def offerWithUglyPhoto = offerPublishedEvent().id("2").title("Bird Seed").photoUrl("http://acme.com/catalog/birdseed.jpg").build()
        def offerWithPrettierPhoto = offerPublishedEvent().id("3").title("Dehydrated Boulders").photoUrl("http://acme.com/catalog/dehydrated.jpg").build()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithPrettyPhoto.photoUrl).returns(0.75).teach()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithUglyPhoto.photoUrl).returns(0.34).teach()
        photoScoreServiceStub(photoScoreService).whenRequested(offerWithPrettierPhoto.photoUrl).returns(0.96).teach()

        when:
        sendOffers([offerWithPrettyPhoto, offerWithUglyPhoto, offerWithPrettierPhoto])

        then:
        getOffers()*.id == ["3", "1"]
    }

    void sendOffers(List<OfferPublishedEvent> toSend) {
        template.postForEntity(OFFERS_RESOURCE, toSend, Void)
    }

    List getOffers() {
        template.getForEntity(OFFERS_RESOURCE, List).body
    }

}