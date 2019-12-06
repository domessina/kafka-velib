package be.technocite.kafkavelib.resource;

import be.technocite.kafkavelib.model.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;

public class VelibStationResource{

    public Collection<Station> getStationsData() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.jcdecaux.com/vls/v1/stations?apiKey=41bb6c4e2cf1d7d65c721181f0e74f39a54d9c4d";
        ResponseEntity<Station[]> response = restTemplate.getForEntity(url, Station[].class);

        Station[] stations = response.getBody();

        return Arrays.asList(stations);
    }
}
