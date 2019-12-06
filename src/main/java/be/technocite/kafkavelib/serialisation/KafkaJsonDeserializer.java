package be.technocite.kafkavelib.serialisation;

import be.technocite.kafkavelib.model.Station;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

// le emodule spring kafka le fait si on ne veut pas s'ennuyer
// nous pouvons transformer cette classe en classe générique afin de la réutiliseer pour différents types json autre que Station..
// ...dans ce cas il faudra passer le
public class KafkaJsonDeserializer implements Deserializer {

    private Logger logger = LogManager.getLogger(this.getClass());

    public KafkaJsonDeserializer() {
    }

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Station deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        Station obj = null;
        try {
            obj = mapper.readValue(bytes, Station.class);
        } catch (Exception e) {

            logger.error(e.getMessage());
        }
        return obj;
    }

    @Override
    public void close() {

    }
}
