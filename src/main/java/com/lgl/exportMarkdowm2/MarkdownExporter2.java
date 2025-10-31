package com.lgl.exportMarkdowm2;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.servlet.http.HttpServletResponse;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

public class MarkdownExporter2 {

    /** Markdown -> HTML */
    public static String markdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create()
        ));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    /** HTML -> 完整 XHTML，并处理代码块换行 */
    public static String wrapAsXhtml(String htmlFragment) {
        Document doc = Jsoup.parse(htmlFragment);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml)
                .escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)
                .charset("UTF-8");
        doc.head().append(
                "<meta charset=\"UTF-8\"/>" +
                        "<style>" +
                        "body { font-family: 'SimSun','Microsoft YaHei','Consolas',monospace; line-height:1.6; }" +
                        "h1,h2,h3,h4,h5 { margin: 1em 0 0.5em; }" +
                        "ul,ol { padding-left:1.6em; }" +
                        "pre { background-color:#f5f5f5; padding:8px; white-space:pre-wrap; word-wrap:break-word; }" +
                        "code { background-color:#f5f5f5; padding:2px 4px; white-space:pre-wrap; }" +
                        "table { border-collapse: collapse; }" +
                        "th,td { border:1px solid #ddd; padding:6px 8px; }" +
                        "</style>"
        );

        // 将 <pre> 内换行符替换为 <br/>
        doc.select("pre").forEach(pre -> {
            String text = pre.text();
            pre.html(text.replace("\n", "<br/>"));
        });

        return doc.outerHtml().replace("<br>", "<br/>");
    }

    /** 导出 Markdown 原文 */
    public static void exportMarkdown(String markdown, String filePath) throws IOException {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) {
            w.write(markdown);
        }
    }

    public static void exportMarkdown(String markdown, HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("text/markdown;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try (OutputStream os = response.getOutputStream()) {
            os.write(markdown.getBytes("UTF-8"));
        }
    }

    /** 导出 PDF */
    public static void exportPdf(String html, String filePath) throws Exception {
        try (OutputStream os = new FileOutputStream(filePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.useFont((FSSupplier<InputStream>) () ->
            {
                try {
                    return new FileInputStream("src/main/resources/SimSun.ttf");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }, "SimSun");
            builder.run();
        }
    }

    public static void exportPdf(String html, HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.useFont((FSSupplier<InputStream>) () ->
            {
                try {
                    return new FileInputStream("src/main/resources/SimSun.ttf");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }, "SimSun");
            builder.run();
        }
    }

    /** 导出 Word，直接用 HTML 渲染 */
    public static void exportWord(String markdown, String filePath) throws Exception {
        String html = markdownToHtml(markdown);
        String xhtml = wrapAsXhtml(html);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(xhtml, null));

        wordMLPackage.save(new File(filePath));
    }

    public static void exportWord(String markdown, HttpServletResponse response, String fileName) throws Exception {
        String html = markdownToHtml(markdown);
        String xhtml = wrapAsXhtml(html);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl xhtmlImporter = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(xhtml, null));

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        wordMLPackage.save(response.getOutputStream());
    }

    /** 测试 */
    public static void main(String[] args) throws Exception {
        String markdown = "# Markdown 文档示例\n\n" +
                "这是一份展示 **Markdown** 各种功能的示例文档。\n\n" +
                "```java\n" +
                "public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, Markdown!\");\n" +
                "    }\n" +
                "}";

        String html = wrapAsXhtml(markdownToHtml(markdown));

        exportMarkdown(markdown, "output.md");
        exportPdf(html, "output.pdf");
        exportWord(markdown, "output.docx");

        System.out.println("导出完成：output.md / output.pdf / output.docx");
    }
}
