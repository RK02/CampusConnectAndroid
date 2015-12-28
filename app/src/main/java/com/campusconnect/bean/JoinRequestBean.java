package com.campusconnect.bean;

/**
 * Created by rkd on 23/12/15.
 */
public class JoinRequestBean {

    private String from_name;

    private String from_pid;

    private String club_name;

    private String timestamp;

    private String requestId;

    private String from_photoUrl;

    private String from_batch;

    public String getFrom_branch() {
        return from_branch;
    }

    public void setFrom_branch(String from_branch) {
        this.from_branch = from_branch;
    }

    public String getFrom_batch() {
        return from_batch;
    }

    public void setFrom_batch(String from_batch) {
        this.from_batch = from_batch;
    }

    private String from_branch;
    public String getFrom_pid() {
        return from_pid;
    }

    public void setFrom_pid(String from_pid) {
        this.from_pid = from_pid;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFrom_photoUrl() {
        return from_photoUrl;
    }

    public void setFrom_photoUrl(String from_photoUrl) {
        this.from_photoUrl = from_photoUrl;
    }

    public String getFrom_name() {

        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }
}
