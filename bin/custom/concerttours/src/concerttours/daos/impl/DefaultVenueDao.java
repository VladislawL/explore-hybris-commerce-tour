package concerttours.daos.impl;

import concerttours.daos.VenueDao;
import concerttours.model.VenueModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("venueDao")
public class DefaultVenueDao implements VenueDao {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    private static final String SELECT_ALL_VENUES_QUERY = "SELECT {v:" + VenueModel.PK + "}" +
            "FROM {" + VenueModel._TYPECODE + " as v}";

    private static final String SELECT_VENUES_BY_CODE_QUERY = "SELECT {v:" + VenueModel.PK + "}" +
            "FROM {" + VenueModel._TYPECODE + " as v} WHERE {v:" + VenueModel.CODE + "} = ?code";

    @Override
    public List<VenueModel> findAllVenues() {
        FlexibleSearchQuery query = new FlexibleSearchQuery(SELECT_ALL_VENUES_QUERY);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }

    @Override
    public List<VenueModel> findVenuesByCode(String code) {
        FlexibleSearchQuery query = new FlexibleSearchQuery(SELECT_VENUES_BY_CODE_QUERY);
        query.addQueryParameter("code", code);
        return flexibleSearchService.<VenueModel>search(query).getResult();
    }
}
