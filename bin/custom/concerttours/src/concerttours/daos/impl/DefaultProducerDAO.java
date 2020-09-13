package concerttours.daos.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.BandModel;
import concerttours.model.ProducerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultProducerDAO implements ProducerDAO {

    @Autowired
    private FlexibleSearchService flexibleSearchService;

    private static final String SELECT_PRODUCER_BY_CODE = "SELECT {p:" + ProducerModel.PK + "}"
            + "FROM {" + ProducerModel._TYPECODE + " AS p} WHERE " + "{p:" + ProducerModel.CODE + "}=?code ";

    @Override
    public List<ProducerModel> findProducerByCode(String code) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(SELECT_PRODUCER_BY_CODE);
        query.addQueryParameter("code", code);
        return flexibleSearchService.<ProducerModel> search(query).getResult();
    }
}
