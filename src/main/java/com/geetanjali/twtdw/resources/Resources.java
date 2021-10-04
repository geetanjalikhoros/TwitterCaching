package com.geetanjali.twtdw.resources;
import com.geetanjali.twtdw.TwitterDropWizardApplication;
import com.geetanjali.twtdw.api.Representation;

import com.codahale.metrics.annotation.Metered;
import com.geetanjali.twtdw.PostTweets;
import com.geetanjali.twtdw.RetrieveTweets;
import com.geetanjali.twtdw.api.Representation;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.jersey.params.*;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import org.slf4j.Marker;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class Resources {
    private final String message;
    private Twitter twitter;
    BeanFactory factory;
    PostTweets ptwt;
    RetrieveTweets twt;
    private static final Logger logger = LoggerFactory.getLogger(TwitterDropWizardApplication.class);

    public Resources(String message) {
        this.message = message;
        this.twitter = TwitterFactory.getSingleton();
        factory=new XmlBeanFactory(new FileSystemResource("applicationContext.xml"));
        ptwt=(PostTweets) factory.getBean("ptwt");
        twt=(RetrieveTweets) factory.getBean("twt");
    }

    @Path("/timeline")
    @GET
    public Response fetchTweet()
    {
        try
        {
            List<Status> statuses = twt.getStatus(twitter);
            int count = statuses.size();
            logger.info("Fetch successful. Displaying Tweets");
            Representation r[] = new Representation[count];
            for(int i=0; i<count; i++)
            {
                System.out.println("Tweet "+ i +"="+ statuses.get(i).getText());
                r[i] = new Representation(Integer.toString(i), statuses.get(i).getText(), statuses.get(i).getUser().getName(),
                        statuses.get(i).getUser().getScreenName(), statuses.get(i).getUser().getProfileImageURL(),
                        statuses.get(i).getCreatedAt());
            }
            Representation.testCache();
            return Response.ok().entity(r).build();
        }
        catch (TwitterException e)
        {
            return Response.serverError().entity("Error in retrieving tweets").build();
        }
    }

    @Path("/tweet")
    @POST
    public Response postTweet()
    {
        try
        {
            ptwt.postStatus(message,twitter);
            return Response.ok().entity("Status updated successfully").build();
        }
        catch (TwitterException e)
        {
            return Response.serverError().entity("Error in updating status").build();
        }
    }
}