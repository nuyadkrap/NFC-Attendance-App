package com.example.san;

public class Notice {
    String noticeName;
    String noticeContent;
    String noticeDate;
    String userName;

    public Notice(String noticeName, String noticeContent, String noticeDate, String userName) {

        this.noticeName = noticeName;
        this.noticeContent= noticeContent;
        this.noticeDate = noticeDate;
        this.userName = userName;
    }

    public String getNoticeName(){
        return  noticeName;}

    public void setNoticeName(String noticeName){
        this. noticeName = noticeName;}

    public String getNoticeContent(){
        return  noticeContent;}

    public void setNoticeContent(String noticeContent){
        this. noticeContent = noticeContent;}

    public String getNoticeDate(){
        return noticeDate;}

    public void setNoticeDate(String noticeDate){
        this.noticeDate =noticeDate;}

    public String getUserName(){
        return userName;}

    public void setUserName(String UserName){
        this.userName =userName;}

}