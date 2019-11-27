package com.example.san;

public class Attendance {

    String courseTitle;
    String userID;
    String userName;
    String courseTime;
    String attdState;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getAttdState() {
        return attdState;
    }

    public void setAttdState(String attdState) {
        this.attdState = attdState;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public Attendance(String courseTitle, String courseTime, String attdState) {
        this.courseTitle = courseTitle;
        this.courseTime = courseTime;
        this.attdState = attdState;
    }

    public Attendance(String courseTitle, String userID, String userName, String courseTime, String attdState) {
        this.courseTitle = courseTitle;
        this.userID = userID;
        this.userName = userName;
        this.courseTime = courseTime;
        this.attdState = attdState;
    }

}
