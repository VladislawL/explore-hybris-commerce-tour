package concerttours.facades.impl;
import concerttours.data.ProducerData;
import concerttours.data.VenueData;
import concerttours.model.ProducerModel;
import concerttours.model.VenueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;

public class DefaultTourFacade implements TourFacade
{
    private ProductService productService;
    @Override
    public TourData getTourDetails(final String tourId)
    {
        if (tourId == null)
        {
            throw new IllegalArgumentException("Tour id cannot be null");
        }
        final ProductModel product = productService.getProductForCode(tourId);
        if (product == null)
        {
            return null;
        }
        // Create a list of ConcertSummaryData from the matches
        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if (product.getVariants() != null)
        {
            for (final VariantProductModel variant : product.getVariants())
            {
                if (variant instanceof ConcertModel)
                {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(convertVenueModelToVenueData(concert.getVenue()));
                    summary.setType(concert.getConcertType() == ConcertType.OPENAIR ? "Outdoors" : "Indoors");
                    summary.setCountDown(concert.getDaysUntil());
                    concerts.add(summary);
                }
            }
        }
        ProducerData producerData = convertProducerModelToProducerData(product.getProducer());
        // Now we can create the TourData transfer object
        final TourData tourData = new TourData();
        tourData.setId(product.getCode());
        tourData.setTourName(product.getName());
        tourData.setDescription(product.getDescription());
        tourData.setConcerts(concerts);
        tourData.setProducer(producerData);
        return tourData;
    }
    @Required
    public void setProductService(final ProductService productService)
    {
        this.productService = productService;
    }

    private VenueData convertVenueModelToVenueData(VenueModel venueModel)
    {
        VenueData venueData = new VenueData();

        venueData.setId(venueModel.getCode());
        venueData.setName(venueModel.getName());
        venueData.setLocation(venueModel.getLocation());
        venueData.setDescription(venueModel.getDescription());

        return venueData;
    }

    private ProducerData convertProducerModelToProducerData(ProducerModel producerModel)
    {
        ProducerData producerData = new ProducerData();

        producerData.setId(producerModel.getCode());
        producerData.setName(producerModel.getName());
        producerData.setSurname(producerModel.getSurname());

        return producerData;
    }
}