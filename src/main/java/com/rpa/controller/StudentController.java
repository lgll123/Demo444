package com.rpa.controller;

import com.rpa.model.Student;
import com.rpa.service.StudentFileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class StudentController {

    private final StudentFileService studentFileService;

    public StudentController(StudentFileService studentFileService) {
        this.studentFileService = studentFileService;
    }

    @GetMapping("/students")
    public List<Student> getStudents() throws IOException {
        return studentFileService.fetchStudentsFromFtp();
    }
}