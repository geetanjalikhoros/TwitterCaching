package com.geetanjali.twtdw;

import twitter4j.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class RetrieveTweets
{
    private List<Status> statuses;

    public RetrieveTweets()
    {
        this.statuses = new ArrayList<Status>();
    }

    public List<Status> getStatus(Twitter twitter) throws TwitterException
    {
        Paging page = new Paging(1,200);
        statuses.addAll(twitter.getHomeTimeline(page));
        return statuses;
    }
}
