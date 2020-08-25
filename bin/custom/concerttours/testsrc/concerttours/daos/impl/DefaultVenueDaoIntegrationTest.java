package concerttours.daos.impl;

import concerttours.daos.VenueDao;
import concerttours.model.VenueModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;

@IntegrationTest
public class DefaultVenueDaoIntegrationTest extends ServicelayerTransactionalTest {

    @Resource
    private VenueDao venueDao;

    @Resource
    private ModelService modelService;

    private static final String VENUE_CODE = "1";
    private static final String VENUE_NAME = "Electric Ballroom";
    private static final String VENUE_DESCRIPTION = "The Electric Ballroom is a 1,500 capacity performance venue";
    private static final String VENUE_LOCATION = "London, UK";

    @Test
    public void shouldFindAllVenues() {
        VenueModel venueModel = createTestVenue();
        modelService.save(venueModel);
        List<VenueModel> venues = venueDao.findAllVenues();

        assertEquals("Venue wasn't saved", 1, venues.size());
    }

    @Test
    public void shouldFindVenuesByCode() {
        VenueModel venueModel = createTestVenue();
        modelService.save(venueModel);
        List<VenueModel> venues = venueDao.findVenuesByCode(VENUE_CODE);

        assertEquals("Venue wasn't saved", 1, venues.size());
        assertEquals(VENUE_CODE, venues.get(0).getCode());
        assertEquals(VENUE_NAME, venues.get(0).getName());
        assertEquals(VENUE_LOCATION, venues.get(0).getLocation());
        assertEquals(VENUE_DESCRIPTION, venues.get(0).getDescription());
    }

    @Test
    public void shouldReturnEmptyListIfNoVenuesFound() {
        List<VenueModel> venues = venueDao.findAllVenues();

        assertEquals("Venue wasn't saved", 0, venues.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfCodeWasNull() {
        venueDao.findVenuesByCode(null);
    }

    private VenueModel createTestVenue() {
        VenueModel venueModel = modelService.create(VenueModel.class);
        venueModel.setCode(VENUE_CODE);
        venueModel.setName(VENUE_NAME);
        venueModel.setDescription(VENUE_DESCRIPTION);
        venueModel.setLocation(VENUE_LOCATION);
        return venueModel;
    }

}
