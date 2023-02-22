package com.example.xmed.sms;

import com.vonage.client.VonageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VonageInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(VonageInit.class);

    public VonageInit(){
        VonageClient client = VonageClient.builder()
                .apiKey("61521526").apiSecret("AR2bl36ItNruzryK").build();

        LOGGER.info("Vonage initialized ... with  API key  61521526");
    }

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder().apiKey("61521526").apiSecret("AR2bl36ItNruzryK").build();
    }
}
