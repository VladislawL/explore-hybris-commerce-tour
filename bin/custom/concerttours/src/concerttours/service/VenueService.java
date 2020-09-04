package concerttours.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface VenueService {
    void updateVenues() throws UnirestException, JsonProcessingException;
}
