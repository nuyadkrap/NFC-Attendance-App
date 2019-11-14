package com.example.san;

public class Attendance {
    String userID;
    String courseTitle;
    String attdState;
    String userName;

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

    public Attendance(String courseTitle, String attdState) {
        this.courseTitle = courseTitle;
        this.attdState = attdState;
    }

    public Attendance(String courseTitle, String attdState, String userID, String userName) {
        this.courseTitle = courseTitle;
        this.attdState = attdState;
        this.userID = userID;
        this.userName = userName;
    }

}
