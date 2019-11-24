package com.example.san;

public class Notice {

    String noticeName;
    String noticeContent;
    String noticeDate;
    String pfName;

    public Notice(String noticeName, String noticeContent, String noticeDate, String pfName) {
        this.noticeName = noticeName;
        this.noticeContent = noticeContent;
        this.noticeDate = noticeDate;
        this.pfName = pfName;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getPfName() {
        return pfName;
    }

    public void setPfName(String pfName) {
        this.pfName = pfName;
    }

}