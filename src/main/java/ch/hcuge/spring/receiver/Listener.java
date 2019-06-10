package ch.hcuge.spring.receiver;

import ch.hcuge.kafka.hcuge.patient.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    // Payload is optional because it can be null when DELETE OPERATION on database
    @KafkaListener(topics = {"${application.topic}"}, groupId = "test")
    public void receive(@Payload(required = false) Envelope data) {
        LOG.info("-------------");
        LOG.info("OPERATION = '{}'", data != null ? data.getOp() : "Unknow opération");
        LOG.info("DATA = '{}'", data != null ? data.getAfter() : "NULL RECEIVED");
        LOG.info("-------------");
    }

}