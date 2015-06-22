package pl.allegro.promo.devoxx2015;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ACMEMostPretty {

    public static void main(String[] args) {
        SpringApplication.run(ACMEMostPretty.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Mongo mongo() {
        return new Fongo("memory").getMongo();
    }
}


