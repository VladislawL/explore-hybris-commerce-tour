package concerttours.service.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;

public class DefaultProducerService implements ProducerService {

    private ProducerDAO producerDAO;

    private static final String UNKNOWN_IDENTIFIER_EXCEPTION_MESSAGE = "Band with code %s not found!";
    private static final String AMBIGUOUS_IDENTIFIER_EXCEPTION_MESSAGE = "Band code %s is not unique, %s bands found!";

    @Override
    public ProducerModel getProducerByCode(String code) {
        List<ProducerModel> producers = producerDAO.findProducerByCode(code);

        if (producers.isEmpty()) {
            throw new UnknownIdentifierException(String.format(UNKNOWN_IDENTIFIER_EXCEPTION_MESSAGE, code));
        } else if (producers.size() > 1) {
            throw new AmbiguousIdentifierException(String.format(AMBIGUOUS_IDENTIFIER_EXCEPTION_MESSAGE, code, producers.size()));
        }

        return producers.get(0);
    }

    public void setProducerDAO(ProducerDAO producerDAO) {
        this.producerDAO = producerDAO;
    }
}
