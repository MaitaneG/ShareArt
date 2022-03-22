package com.example.shareart.models;

import java.util.ArrayList;

public class FCMResponse {

    private long multicast_id;
    private int success;
    private int failure;
    private int canonicals_ids;
    ArrayList<Object> results = new ArrayList<>();

    public FCMResponse(long multicast_id, int success, int failure, int canonicals_ids, ArrayList<Object> results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.canonicals_ids = canonicals_ids;
        this.results = results;
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonicals_ids() {
        return canonicals_ids;
    }

    public void setCanonicals_ids(int canonicals_ids) {
        this.canonicals_ids = canonicals_ids;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
