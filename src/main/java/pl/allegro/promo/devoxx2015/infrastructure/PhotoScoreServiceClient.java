package pl.allegro.promo.devoxx2015.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.client.RestTemplate;
import pl.allegro.promo.devoxx2015.domain.PhotoScoreSource;

public class PhotoScoreServiceClient implements PhotoScoreSource {

    private final String serviceUrl;
    private final RestTemplate template;

    public PhotoScoreServiceClient(String serviceUrl, RestTemplate template) {
        this.serviceUrl = serviceUrl;
        this.template = template;
    }

    @Override
    public double getScore(String photoUrl) {
        return template.getForEntity(serviceUrl + "/score?photoUrl={url}", PhotoScoreResponse.class, photoUrl)
                .getBody()
                .getScore();
    }

    private static class PhotoScoreResponse {

        private final double score;

        @JsonCreator
        public PhotoScoreResponse(@JsonProperty("score") double score) {
            this.score = score;
        }

        public double getScore() {
            return score;
        }

    }

}
