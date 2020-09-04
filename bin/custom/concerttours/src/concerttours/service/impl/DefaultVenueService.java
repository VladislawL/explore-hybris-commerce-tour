package concerttours.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import concerttours.daos.VenueDao;
import concerttours.model.VenueModel;
import concerttours.service.VenueService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Locale;

public class DefaultVenueService implements VenueService {
    private ConfigurationService configurationService;
    private ModelService modelService;
    private VenueDao venueDao;

    private static final String APIKEY_PROPERTY = "venue.service.apikey";
    private static final String API_URL = "https://api.songkick.com/api/3.0/search/venues.json?query=Minsk&apikey=%s&per_page=100";
    private static final String API_ACCESS_EXCEPTION_MESSAGE = "Unable to access api endpoint by %s";

    @Override
    public void updateVenues() throws JsonProcessingException {
        String apikey = configurationService.getConfiguration().getString(APIKEY_PROPERTY);

        try {
            HttpResponse<String> response = Unirest.get(String.format(API_URL, apikey)).asString();

            updateVenues(response);
        } catch (UnirestException e) {
            String apiURL = String.format(API_URL, apikey);
            throw new ResourceAccessException(String.format(API_ACCESS_EXCEPTION_MESSAGE, apiURL));
        }
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
        List<VenueModel> venueList = venueDao.findVenuesByCode(jsonVenue.get("id").asText());
        if (venueList.isEmpty()) {
            VenueModel venueModel = modelService.create(VenueModel.class);

            return setVenueModelValues(venueModel, jsonVenue);
        } else {
            VenueModel venueModel = venueList.get(0);

            return setVenueModelValues(venueModel, jsonVenue);
        }
    }

    private VenueModel setVenueModelValues(VenueModel venueModel, JsonNode jsonVenue) {
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

    public void setVenueDao(VenueDao venueDao) {
        this.venueDao = venueDao;
    }
}
