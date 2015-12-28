package com.campusconnect.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by canopus on 30/11/15.
 */
public class MyFeedBean implements Serializable {


    String eventCreator;
    String description;
    String views;
    String photo ;
    String clubid ;

    String pid ;
    String timeStamp;
    String title ;
    String collegeId;
    String kind ;

    String venue;
    String start_date;
    String start_time;
    String end_date;
    String end_time;

    ArrayList attendees;
    String completed;
    ArrayList tag;
    String likers;
    String clubphoto;

    String ClubAbbreviation;


    Boolean isLike;

    public String getClubAbbreviation() {
        return ClubAbbreviation;
    }

    public void setClubAbbreviation(String clubAbbreviation) {
        ClubAbbreviation = clubAbbreviation;
    }



    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public ArrayList getTag() {
        return tag;
    }

    public void setTag(ArrayList tag) {
        this.tag = tag;
    }

    public String  getLikers() {
        return likers;
    }

    public void setLikers(String likers) {
        this.likers = likers;
    }

    public String getClubphoto() {
        return clubphoto;
    }

    public void setClubphoto(String clubphoto) {
        this.clubphoto = clubphoto;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public ArrayList getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList attendees) {
        this.attendees = attendees;
    }

    public String getEventCreator() {
        return eventCreator;
    }

    public void setEventCreator(String eventCreator) {
        this.eventCreator = eventCreator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getClubid() {
        return clubid;
    }

    public void setClubid(String clubid) {
        this.clubid = clubid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }




}
