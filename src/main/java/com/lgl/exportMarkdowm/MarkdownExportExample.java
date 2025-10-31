//package com.lgl.exportMarkdowm;
//
//import java.io.IOException;
//
//public class MarkdownExportExample {
//    public static void main(String[] args) {
//        // 示例Markdown内容
//        String markdownContent = "# 标题\n\n" +
//                "这是一个由大模型生成的Markdown文档。\n\n" +
//                "## 二级标题\n\n" +
//                "- 列表项1\n" +
//                "- 列表项2\n" +
//                "- 列表项3\n\n" +
//                "**粗体文本** *斜体文本*\n\n" +
//                "[链接示例](https://example.com)";
//
//        try {
//            // 导出为单个格式
//            MarkdownExporter.exportMarkdown(markdownContent, "target/document.pdf", "pdf");
//            MarkdownExporter.exportMarkdown(markdownContent, "target/document.docx", "docx");
//            MarkdownExporter.exportMarkdown(markdownContent, "target/document.md", "md");
//
//            // 或者批量导出所有格式
////            MarkdownExporter.exportAllFormats(markdownContent, "output/document");
//
//            System.out.println("导出成功！");
//        } catch (IOException e) {
//            System.err.println("导出失败: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}