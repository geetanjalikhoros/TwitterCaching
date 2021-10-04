package com.geetanjali.twtdw;
//import com.geetanjali.twtdw.health.HealthCheckup;
import com.geetanjali.twtdw.resources.Resources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TwitterDropWizardApplication extends Application<TwitterDropWizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TwitterDropWizardApplication().run(args);
    }

    /*@Override
    public String getName() {
        return "Hello World";
    }*/

    @Override
    public void initialize(final Bootstrap<TwitterDropWizardConfiguration> bootstrap) {

    }

    @Override
    public void run(final TwitterDropWizardConfiguration configuration,
                    final Environment environment) {
        final Resources resource = new Resources(configuration.getMessage());
       // final HealthCheckup healthCheck =
        //        new HealthCheckup(configuration.getTemplate());
        // environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

}
