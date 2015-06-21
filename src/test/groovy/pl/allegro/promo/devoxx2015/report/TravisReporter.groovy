package pl.allegro.promo.devoxx2015.report

import org.springframework.web.client.RestTemplate

class TravisReporter {
    
    private static final String REMOTE = "https://allegrotech.herokuapp.com/check/"
    
    private final RestTemplate client = new RestTemplate()
    
    void report() {
        Map env = System.getenv()
        
        if(env['TRAVIS']) {
            client.postForEntity(REMOTE + env['TRAVIS_JOB_ID'], "{\"repository\": \"${env['TRAVIS_REPO_SLUG']}\" }", String.class)
        }
    }
    
}
