package com.lgl.model;

/**
 * 定义请求/响应结构
 * @Author 刘国良
 * @Date 2025/5/8 8:52
 * @Destription
 */

public class Message {
    private String role;
    private String content;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

