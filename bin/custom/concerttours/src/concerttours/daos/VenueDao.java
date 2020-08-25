package concerttours.daos;

import concerttours.model.VenueModel;

import java.util.List;

public interface VenueDao {
    List<VenueModel> findAllVenues();
    List<VenueModel> findVenuesByCode(String code);
}
