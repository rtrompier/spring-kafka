package ch.hcuge.kafka.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.ZonedDateTime;

@Data
@Builder
@ToString
public class Patient {
    private String firstname;
    private String lastname;
    private ZonedDateTime birthdate;
}
