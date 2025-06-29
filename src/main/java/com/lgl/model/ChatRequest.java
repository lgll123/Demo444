package com.lgl.model;

import java.util.List;

/**
 * 请求体的数据内部结构，用于构建请求 JSON
 * @Author 刘国良
 * @Date 2025/5/8 8:54
 * @Destription
 */

public class ChatRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;

    public ChatRequest(String model, List<Message> messages, double temperature, int max_tokens) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_tokens = max_tokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMax_tokens() {
        return max_tokens;
    }

    public void setMax_tokens(int max_tokens) {
        this.max_tokens = max_tokens;
    }
}

