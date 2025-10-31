package com.rpa.service;

import com.rpa.model.Student;
import com.rpa.util.FtpUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentFileService {

    public List<Student> fetchStudentsFromFtp() throws IOException {
        String ftpHost = "192.168.122.128";
        int ftpPort = 21;
        String ftpUser = "lgl";
        String ftpPass = "l15170020818";
        String ftpDir = "/home/rpa";

        // 从 FTP 下载文件内容
        String fileContent = FtpUtil.downloadFileAsString(ftpHost, ftpPort, ftpUser, ftpPass, ftpDir);

        // 解析文本
        List<Student> students = new ArrayList<>();
        for (String line : fileContent.split("\\r?\\n")) {
            line = line.trim();
            if (line.isEmpty()) continue; // 跳过空行
            if (line.startsWith("|")) line = line.substring(1); // 去掉开头的 |
            if (line.endsWith("|")) line = line.substring(0, line.length() - 1); // 去掉结尾的 |

            String[] fields = line.split("\\|", -1); // -1 保留空字段
            String name = fields.length > 0 ? fields[0] : "";
            String grade = fields.length > 1 ? fields[1] : "";
            String phone = fields.length > 2 ? fields[2] : "";
            String email = fields.length > 3 ? fields[3] : "";

            students.add(new Student(name, grade, phone, email));
        }

        return students;
    }
}