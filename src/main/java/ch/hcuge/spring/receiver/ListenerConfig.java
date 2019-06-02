package ch.hcuge.spring.receiver;

import ch.hcuge.kafka.Patient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@Configuration
//@EnableKafka
public class ListenerConfig {

    @Value("${application.bootstrap-servers}")
    private String bootstrapServers;

//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
////        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
//        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
////        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        return props;
//    }

//    @Bean
//    public ConsumerFactory<Integer, Patient> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
////        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
//        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
////        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<Integer, Patient> kafkaConsumerFactory() {
//        ConcurrentKafkaListenerContainerFactory<Integer, Patient> factory = new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }

//    @Bean
//    public ConsumerFactory<Integer, Patient> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new IntegerDeserializer(), new KafkaAvroDeserializer<>(Patient.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<Integer, Patient> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, kafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }

}