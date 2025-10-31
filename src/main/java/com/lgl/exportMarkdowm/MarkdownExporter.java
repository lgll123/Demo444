//package com.lgl.exportMarkdowm;
//
//import org.commonmark.node.*;
//import org.commonmark.parser.Parser;
//import org.commonmark.renderer.html.HtmlRenderer;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.tool.xml.XMLWorkerHelper;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//public class MarkdownExporter {
//
//    /**
//     * 导出Markdown到文件
//     * @param markdownContent Markdown内容
//     * @param outputPath 输出文件路径
//     * @param format 格式：pdf, docx, md
//     */
//    public static void exportMarkdown(String markdownContent, String outputPath, String format) throws IOException {
//        switch (format.toLowerCase()) {
//            case "pdf":
//                exportToPdf(markdownContent, outputPath);
//                break;
//            case "docx":
//                //支持更多格式的导出
//                convertMarkdownToWord(markdownContent, outputPath);
//                break;
//            case "md":
//                exportToMarkdown(markdownContent, outputPath);
//                break;
//            default:
//                throw new IllegalArgumentException("不支持的格式: " + format);
//        }
//    }
//
//    /**
//     * 导出为PDF
//     */
//    private static void exportToPdf(String markdownContent, String outputPath) throws IOException {
//        // 将Markdown转换为HTML
//        String htmlContent = convertMarkdownToHtml(markdownContent);
//
//        try (OutputStream os = new FileOutputStream(outputPath)) {
//            Document document = new Document(PageSize.A4);
//            PdfWriter writer = PdfWriter.getInstance(document, os);
//            document.open();
//
//            // 使用XMLWorkerHelper将HTML转换为PDF
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//                    new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)),
//                    null, StandardCharsets.UTF_8);
//
//            document.close();
//        } catch (Exception e) {
//            throw new IOException("PDF导出失败", e);
//        }
//    }
//
//    /**
//     * 导出为Markdown文件
//     */
//    private static void exportToMarkdown(String markdownContent, String outputPath) throws IOException {
//        Files.write(Paths.get(outputPath), markdownContent.getBytes(StandardCharsets.UTF_8));
//    }
//
//    /**
//     * 将Markdown转换为HTML
//     */
//    private static String convertMarkdownToHtml(String markdown) {
//        Parser parser = Parser.builder().build();
//        Node document = parser.parse(markdown);
//        HtmlRenderer renderer = HtmlRenderer.builder().build();
//        return renderer.render(document);
//    }
//
//    /**
//     * 批量导出所有格式
//     */
//    public static void exportAllFormats(String markdownContent, String basePath) throws IOException {
//        exportMarkdown(markdownContent, basePath + ".pdf", "pdf");
//        exportMarkdown(markdownContent, basePath + ".docx", "docx");
//        exportMarkdown(markdownContent, basePath + ".md", "md");
//    }
//
//
//
//
//
//
//    //导出为word--高级版本（支持更好的格式保留）
//    public static void convertMarkdownToWord(String markdown, String outputPath) throws IOException {
//        try (XWPFDocument document = new XWPFDocument();
//             FileOutputStream out = new FileOutputStream(outputPath)) {
//
//            Parser parser = Parser.builder().build();
//            Node node = parser.parse(markdown);
//
//            MarkdownVisitor visitor = new MarkdownVisitor(document);
//            node.accept(visitor);
//
//            document.write(out);
//        }
//    }
//    private static class MarkdownVisitor extends AbstractVisitor {
//        private final XWPFDocument document;
//        private XWPFParagraph currentParagraph;
//        private XWPFRun currentRun;
//
//        public MarkdownVisitor(XWPFDocument document) {
//            this.document = document;
//        }
//
//        @Override
//        public void visit(Heading heading) {
//            currentParagraph = document.createParagraph();
//            currentRun = currentParagraph.createRun();
//            currentRun.setBold(true);
//            currentRun.setFontSize(16 + (4 - heading.getLevel()) * 2);
//        }
//
//        @Override
//        public void visit(Text text) {
//            if (currentRun != null) {
//                currentRun.setText(text.getLiteral());
//            }
//        }
//
//        @Override
//        public void visit(Emphasis emphasis) {
//            if (currentRun != null) {
//                currentRun.setItalic(true);
//            }
//        }
//
//        @Override
//        public void visit(StrongEmphasis strongEmphasis) {
//            if (currentRun != null) {
//                currentRun.setBold(true);
//            }
//        }
//
//        // 可以继续添加其他节点的处理方法...
//    }
//}