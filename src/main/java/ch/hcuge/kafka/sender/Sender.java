package ch.hcuge.kafka.sender;

import ch.hcuge.kafka.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, Patient> kafkaTemplate;

    public void send(Patient patient) {
        LOGGER.info("sending patient='{}'", patient);
        kafkaTemplate.send("patient", patient);
    }
}
