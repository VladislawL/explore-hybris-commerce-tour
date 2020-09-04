package concerttours.service;

import com.mashape.unirest.http.exceptions.UnirestException;

public interface VenueService {
    void updateVenues() throws UnirestException;
}
