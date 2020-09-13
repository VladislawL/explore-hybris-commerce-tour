package concerttours.service;

import concerttours.model.ProducerModel;

public interface ProducerService {
    ProducerModel getProducerByCode(String code);
}
