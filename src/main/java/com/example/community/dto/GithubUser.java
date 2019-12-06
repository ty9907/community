package com.example.community.dto;

public class GithubUser {

    private String name;//GitHub用户名称
    private long id;//用户的Github id,唯一标识
    private String bio;//用户描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}

