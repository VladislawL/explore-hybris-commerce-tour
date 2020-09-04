package concerttours.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import concerttours.model.VenueModel;
import concerttours.service.VenueService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Locale;

public class DefaultVenueService implements VenueService {
    private ConfigurationService configurationService;
    private ModelService modelService;

    private static final String APIKEY_PROPERTY = "venue.service.apikey";
    private static final String API_URL = "https://api.songkick.com/api/3.0/search/venues.json?query=Minsk&apikey=%s&per_page=100";

    @Override
    public void updateVenues() throws UnirestException, JsonProcessingException {
        String apikey = configurationService.getConfiguration().getString(APIKEY_PROPERTY);
        HttpResponse<String> response = Unirest.get(String.format(API_URL, apikey)).asString();

        updateVenues(response);
    }

    private void updateVenues(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode venues = mapper.readTree(response.getBody())
                .path("resultsPage")
                .path("results")
                .path("venue");

        for (JsonNode jsonVenue : venues) {
            VenueModel venueModel = createVenueModelFromJson(jsonVenue);
            modelService.save(venueModel);
        }
    }

    private VenueModel createVenueModelFromJson(JsonNode jsonVenue) {
        VenueModel venueModel = modelService.create(VenueModel.class);

        String zip = jsonVenue.get("zip").asText();
        String street = jsonVenue.get("street").asText();
        String city = jsonVenue
                .path("city")
                .get("displayName").asText();

        venueModel.setCode(jsonVenue.get("id").asText());
        venueModel.setName(jsonVenue.get("displayName").asText(), Locale.ENGLISH);
        venueModel.setDescription(jsonVenue.get("description").asText());
        venueModel.setLocation(String.join(",", zip, street, city));

        return venueModel;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
