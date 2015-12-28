package com.campusconnect.bean;

/**
 * Created by rkd on 23/12/15.
 */
public class SuperAdminRequestBean {
    String from_name;
    String description;
    String from_pid;
    String club_name;
    String timestamp;
    String abbreviation;
    String requestId;
    String from_photoUrl;

    public String getIsAlumni() {
        return isAlumni;
    }

    public void setIsAlumni(String isAlumni) {
        this.isAlumni = isAlumni;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    String isAlumni;
}
