package com.example.san;


public class AttendList {
  //  String userID;
  // String courseID;
    String courseTitle;
    String courseRoom;
    String courseTime;

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public AttendList(String courseTitle, String courseRoom, String courseTime) {
        this.courseTitle = courseTitle;
        this.courseRoom = courseRoom;
        this.courseTime = courseTime;
    }
}

