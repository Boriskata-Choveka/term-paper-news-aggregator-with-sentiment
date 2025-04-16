package org.example.models;

import org.bson.types.ObjectId;

import java.util.Date;

public class Article {
    private ObjectId id;
    private String title;
    private String content;
    private String source;
    private Date date;
    private int sentimentScore;

    public Article() { }

    public Article(String title, String content, String source, Date date, int sentimentScore) {
        this.title = title;
        this.content = content;
        this.source = source;
        this.date = date;
        this.sentimentScore = sentimentScore;
    }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getSentimentScore() {
        return sentimentScore;
    }
    public void setSentimentScore(int sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}
