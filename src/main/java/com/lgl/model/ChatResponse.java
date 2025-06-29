package com.lgl.model;

import java.util.List;

/**
 * 按着响应的JSON格式来设计响应体
 * 用来封装响应体字符串
 * @Author 刘国良
 * @Date 2025/5/8 8:55
 * @Destription
 */

public class ChatResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}

