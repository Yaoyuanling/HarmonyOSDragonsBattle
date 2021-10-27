package com.example.snake.snake;

public class User {
    private Integer id;
    private String name;
    private int maxScore;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getMaxScore() {
        return maxScore;
    }
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", maxScore=" + maxScore + "]";
    }

}

