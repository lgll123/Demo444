package com.rpa.model;

import lombok.Data;

@Data
public class Student {

    private String name;
    private String grade;
    private String phone;
    private String email;

    // 构造器
    public Student() {}
    public Student(String name, String grade, String phone, String email) {
        this.name = name;
        this.grade = grade;
        this.phone = phone;
        this.email = email;
    }

}
