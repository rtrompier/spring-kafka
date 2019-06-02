package ch.hcuge.spring.receiver;

import ch.hcuge.kafka.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    @KafkaListener(topics = {"${application.topic}"}, groupId = "test")
    public void receive(@Payload Patient data,
                        @Headers MessageHeaders headers) {

        LOG.info("received data='{}'", data);
//        headers.keySet().forEach(key -> {
//            LOG.info("{}: {}", key, headers.get(key));
//        });

    }

}