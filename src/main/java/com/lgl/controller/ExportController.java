package com.lgl.controller;

import com.lgl.util.WordExportUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/export")
public class ExportController {

    /**
     * 导出富文本数据到word文档中
     */
    @GetMapping("/word")
    public ResponseEntity<ByteArrayResource> exportToWord(
            @RequestParam String title,
            @RequestParam String summary,
            @RequestParam String content) throws Exception {

        byte[] wordBytes = WordExportUtil.exportKnowledgeToWord(title,  summary, content);

        ByteArrayResource resource = new ByteArrayResource(wordBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=knowledge.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(wordBytes.length)
                .body(resource);
    }
}