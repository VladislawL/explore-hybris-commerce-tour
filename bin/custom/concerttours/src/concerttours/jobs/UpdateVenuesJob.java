package concerttours.jobs;

import com.mashape.unirest.http.exceptions.UnirestException;
import concerttours.service.VenueService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;
import org.springframework.web.client.ResourceAccessException;

public class UpdateVenuesJob extends AbstractJobPerformable<CronJobModel> {
    private static final Logger LOGGER = Logger.getLogger(SendNewsJob.class);
    private VenueService venueService;

    @Override
    public PerformResult perform(CronJobModel cronJobModel) {
        LOGGER.info("Updating venues");
        try {
            venueService.updateVenues();
            LOGGER.info("Updating finished successfully");
            return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage(), e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        } catch (UnirestException e) {
            LOGGER.error("Unable to update venues", e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
    }

    public void setVenueService(VenueService venueService) {
        this.venueService = venueService;
    }
}
