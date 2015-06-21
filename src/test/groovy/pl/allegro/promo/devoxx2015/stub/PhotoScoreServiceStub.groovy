package pl.allegro.promo.devoxx2015.stub

import com.github.tomakehurst.wiremock.WireMockServer

import static com.github.tomakehurst.wiremock.client.WireMock.*

class PhotoScoreServiceStub {

    private final WireMockServer server
    private String link
    private double score
    private boolean fail

    PhotoScoreServiceStub(WireMockServer server) {
        this.server = server
    }

    static PhotoScoreServiceStub photoScoreServiceStub(WireMockServer server) {
        return new PhotoScoreServiceStub(server)
    }

    PhotoScoreServiceStub whenRequested(String link) {
        this.link = link
        return this
    }

    PhotoScoreServiceStub fails() {
        this.fail = true
        return this
    }

    PhotoScoreServiceStub returns(double score) {
        this.score = score
        return this
    }

    void teach() {
        if (fail) {
            server.stubFor(get(urlEqualTo("/score?photoUrl=$link")).willReturn(aResponse().withStatus(500)))
        } else {
            server.stubFor(get(urlEqualTo("/score?photoUrl=$link"))
                    .willReturn(aResponse().withBody("""{ "score": $score}""")
                    .withHeader("Content-Type", "application/json"))
            )
        }
    }
}
