package com.zero.foodie.model;

public class RecipeBrief {

    private  String id;
    private  String name;
    private  String imageURL;
    private  String briefIntro;
    private  String link;

    public RecipeBrief(String id, String name, String imageURL, String briefIntro, String link) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.briefIntro = briefIntro;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public String getLink() {
        return link;
    }




}
