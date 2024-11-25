package com.ll.domain.entity;

public class WiseSaying {
    private int id;
//    private final int id;
    private String quotes;
    private String author;

    public WiseSaying(String quotes, String author) {
//        this.id = id;
        this.quotes = quotes;
        this.author = author;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getQuotes() {
        return quotes;
    }

    public String getAuthor() {
        return author;
    }

    public void setQuotes(String quotes) {
        this.quotes = quotes;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}