package ch.hcuge.kafka.scheduler;

import ch.hcuge.kafka.model.Patient;
import ch.hcuge.kafka.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class Scheduler {

    @Autowired
    private Sender sender;

    @Scheduled(fixedDelay = 10000)
    void send() {
        this.sender.send(Patient.builder()
                .firstname("Remy")
                .lastname("Trompier")
                .birthdate(ZonedDateTime.now())
                .build());
    }
}
