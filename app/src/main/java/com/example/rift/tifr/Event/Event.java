package com.example.rift.tifr.Event;


import java.io.Serializable;

public class Event implements Serializable {

    private String msDate;
    private String msTime;
    private String mTitle;
    private String mUrl;
    private String mDescription;
    private String mRoom;
    private String mId;
    private String mImage;
    private String meDate;
    private String meTime;

    /**
     * Construct {@link Event}
     *@param id
     * @param title
     * @param sdate
     * @param stime
     * @paran description
     * @param room
     * @param url
     * @param image
     */
    public Event(String id,String title,String sdate,String stime, String description,String room,
                 String url, String image, String edate, String etime) {
        msDate = sdate;
        msTime = stime;
        mTitle = title;
        mUrl = url;
        mDescription = description;
        mRoom=room;
        mId=id;
        mImage=image;
        meDate = edate;
        meTime = etime;
    }



    /**
     * Return date of event in String format.
     *
     * @return
     */
    public String getDate() {
        return msDate;
    }
    public void setDate(String sdate){this.msDate=sdate;}

    /**
     * Return time of event in String format.
     *
     * @return
     */
    public String getTime() {
        return msTime;
    }
    public void setTime(String stime){this.msTime=stime;}

    /**
     * Return date of event in String format.
     *
     * @return
     */
    public String geteDate() {
        return meDate;
    }
    public void seteDate(String edate){this.meDate=edate;}

    /**
     * Return time of event in String format.
     *
     * @return
     */
    public String geteTime() {
        return meTime;
    }
    public void seteTime(String etime){this.meTime=etime;}


    /**
     * Return title of the event.
     *
     * @return
     */
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title){this.mTitle=title;}

    /**
     * Return url of the event.
     *
     * @return
     */
    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String url){this.mUrl=url;}


    /**
     * Return description of the event.
     *
     * @return
     */
    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String description){this.mDescription= description;}

    /**
     * Return room of the event.
     *
     * @return
     */
    public String getRoom() {
        return mRoom;
    }
    public void setRoom(String room){this.mRoom=room;}

    /**
     * Return id of the event.
     *
     * @return
     */
    public String getId() {
        return mId;
    }
    public void setId(String id){this.mId=id;}

    /**
     * Return image of the event.
     *
     * @return
     */
    public String getImage() {
        return mImage;
    }
    public void setImage(String image){this.mImage=image;}


}