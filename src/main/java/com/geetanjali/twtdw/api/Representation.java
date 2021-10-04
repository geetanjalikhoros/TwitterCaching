package com.geetanjali.twtdw.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class Representation
{
    private String message;
    private String username;
    private String twitterHandle;
    private String profileImageURL;
    private Date createdAt;
    private static LoadingCache<String, Representation> repCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Representation>() {
                @Override
                public Representation load(String id) throws Exception {
                    return getFromDatabase(id);
                }
            });
    private static Map<String, Representation> database = new HashMap<String, Representation>();

    public Representation(String tid, String message, String username, String twitterHandle,
                          String profileImageURL, Date createdAt)
    {
        this.message = message;
        this.username = username;
        this.twitterHandle = twitterHandle;
        this.profileImageURL = profileImageURL;
        this.createdAt = createdAt;

        database.put(tid, this);
    }

    private static Representation getFromDatabase(String id)
    {
        System.out.println("Database hit for " + id);
        return database.get(id);
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getTwitterHandle() {
        return twitterHandle;
    }

    @JsonProperty
    public String getProfileImageURL() {
        return profileImageURL;
    }

    @JsonProperty
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString()
    {
        return "Tweets{ Message:" + message + " Username:" + username + " Twitter Handle:" + twitterHandle + " Profile image URL:"
                + profileImageURL + " Created At:" + createdAt + " }";
    }

    public static void testCache()
    {
        try {
            System.out.println("First call");
            System.out.println(repCache.get("100") + "\n");

            System.out.println("Second call");
            System.out.println(repCache.get("100") + "\n");
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

