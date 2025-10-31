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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownExporter {

    /**
     * Markdown 转 HTML
     */
    public static String markdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(
                TablesExtension.create(),
                StrikethroughExtension.create()
        ));

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdown);
        String html = renderer.render(document);

        // 包装为完整 HTML
        Document jsoupDoc = Jsoup.parse(html);
        jsoupDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        return jsoupDoc.html();
    }

    // 3) 导出 Markdown 原文
    public static void exportMarkdown(String markdown, String filePath) throws IOException {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8")) {
            w.write(markdown);
        }
    }

    /**
     * 导出 Markdown 文件（浏览器下载）
     */
    public static void exportMarkdown(String markdown, HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("text/markdown;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        try (OutputStream os = response.getOutputStream()) {
            os.write(markdown.getBytes("UTF-8"));
        }
    }

    /**
     * 导出 PDF（RC9 版本）
     */
    public static void exportPdf(String html, String filePath) throws Exception {
        try (OutputStream os = new FileOutputStream(new File(filePath))) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // 设置 HTML
            builder.withHtmlContent(html, null);

            // 指定输出
            builder.toStream(os);

            // ⚠️ 指定中文字体（必须手动设置，不然中文变 #）
            // 你可以换成 C:/Windows/Fonts/msyh.ttc 或者 Linux 的 /usr/share/fonts/noto-cjk/NotoSansCJK-Regular.ttc
            builder.useFont(
                    (FSSupplier<InputStream>) () -> {
                        try {
                            return new FileInputStream("src/main/resources/SimSun.ttf");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    "SimSun"
            );
            // 开始渲染
            builder.run();
        }
    }

    /**
     * 导出 PDF 文件（浏览器下载）
     */
    public static void exportPdf(String html, HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        try (OutputStream os = response.getOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);

            // ⚠️ 指定中文字体（要确保字体存在）
            builder.useFont(
                    (FSSupplier<InputStream>) () -> {
                        try {
                            return new FileInputStream("C:/Windows/Fonts/simsun.ttf");
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    "SimSun"
            );
            builder.run();
        }
    }


    // Demo
    public static void main(String[] args) throws Exception {
        String markdown = "# Markdown 文档示例\n" +
                "\n" +
                "这是一份展示 **Markdown** 各种功能的示例文档。\n" +
                "\n" +
                "## 二级标题\n" +
                "\n" +
                "Markdown 是一种轻量级标记语言，它允许人们使用易读易写的纯文本格式编写文档。\n" +
                "\n" +
                "### 三级标题\n" +
                "\n" +
                "下面展示一些常用的 Markdown 语法元素：\n" +
                "\n" +
                "#### 列表功能\n" +
                "\n" +
                "**无序列表：**\n" +
                "- 列表项一\n" +
                "- 列表项二\n" +
                "  - 嵌套列表项 A\n" +
                "  - 嵌套列表项 B\n" +
                "- 列表项三\n" +
                "\n" +
                "**有序列表：**\n" +
                "1. 第一项\n" +
                "2. 第二项\n" +
                "   1. 嵌套有序项\n" +
                "   2. 另一个嵌套项\n" +
                "3. 第三项\n" +
                "\n" +
                "#### 文本格式\n" +
                "\n" +
                "- **粗体文本**\n" +
                "- *斜体文本*\n" +
                "- ***粗斜体文本***\n" +
                "- ~~删除线文本~~\n" +
                "- `行内代码`\n" +
                "\n" +
                "#### 链接和图片\n" +
                "\n" +
                "这是一个[链接示例](https://www.example.com)，指向示例网站。\n" +
                "\n" +
                "![图片描述](https://via.placeholder.com/150x50?text=Placeholder+Image)\n" +
                "\n" +
                "#### 代码块\n" +
                "\n" +
                "```java\n" +
                "public class HelloWorld {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello, Markdown!\");\n" +
                "        // 这是一段Java代码示例\n" +
                "        int number = 42;\n" +
                "        String text = \"示例文本\";\n" +
                "    }\n" +
                "}";

        String markdownToHtml = markdownToHtml(markdown);
        // 2) 片段 -> 完整 XHTML（关键）
        String xhtml = wrapAsXhtml(markdownToHtml);

        //导出
        exportMarkdown(markdown, "output.md");
        exportPdf(xhtml, "output.pdf");
        exportWord(markdown, "output.docx");

        System.out.println("导出完成：output.md / output.pdf / output.docx");
    }

    // 2) HTML 片段 -> 完整 XHTML 文档（关键修复点）
    public static String wrapAsXhtml(String htmlFragment) {
        // 用 jsoup 自动补齐 <html><head><body> 结构，并以 XML/XHTML 方式输出
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(htmlFragment);
        jsoupDoc.outputSettings()
                .syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
                .escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)
                .charset("UTF-8");

        // 在 <head> 中加一些基础样式（可选）
        jsoupDoc.head().append(
                "<meta charset=\"UTF-8\"/>" +
                        "<style>" +
                        "body { font-family: \"SimSun\", \"Microsoft YaHei\", \"Noto Sans CJK SC\", Arial, sans-serif; line-height: 1.6; }" +
                        "h1,h2,h3,h4 { margin: 1em 0 .5em; }" +
                        "ul,ol { padding-left: 1.6em; }" +
                        "code, pre {" +
                        "  font-family: \"SimSun\", \"Microsoft YaHei\", \"Noto Sans Mono CJK SC\", \"Consolas\", monospace;" +
                        "  background: #f5f5f5;" +
                        "  padding: 2px 4px;" +
                        "  border-radius: 3px;" +
                        "}" +
                        "pre { padding: 8px; overflow: auto; }" +
                        "table { border-collapse: collapse; }" +
                        "th, td { border: 1px solid #ddd; padding: 6px 8px; }" +
                        "</style>"
        );

        return jsoupDoc.outerHtml()
                // 一些 HTML5 自闭合标签在 XHTML 下需要闭合
                .replace("<br>", "<br/>");
    }



    /**
     * 将 Markdown 文本导出为 Word 文件
     */
    public static void exportWord(String markdown, String filePath) throws IOException {
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(filePath)) {

            String[] lines = markdown.split("\n");

            boolean inCodeBlock = false;
            StringBuilder codeBuffer = new StringBuilder();
            int orderedListCounter = 1;

            for (String line : lines) {
                line = line.replace("\r", ""); // 清理回车

                // --- 检测代码块 ---
                if (line.startsWith("```")) {
                    if (!inCodeBlock) {
                        inCodeBlock = true;
                        codeBuffer.setLength(0); // 开始新代码块，清空缓存
                    } else {
                        inCodeBlock = false;
                        writeCodeBlock(document, codeBuffer.toString());
                    }
                    continue;
                }

                if (inCodeBlock) {
                    codeBuffer.append(line).append("\n");
                    continue;
                }

                XWPFParagraph paragraph = document.createParagraph();

                // --- 标题 ---
                if (line.startsWith("### ")) {
                    createStyledRun(paragraph, line.substring(4), 14, true, false);
                } else if (line.startsWith("## ")) {
                    createStyledRun(paragraph, line.substring(3), 16, true, false);
                } else if (line.startsWith("# ")) {
                    createStyledRun(paragraph, line.substring(2), 20, true, false);
                }
                // --- 无序列表 ---
                else if (line.startsWith("- ")) {
                    parseInlineMarkdown(paragraph, "• " + line.substring(2), 12);
                }
                // --- 有序列表 ---
                else if (line.matches("^\\d+\\.\\s+.*")) {
                    parseInlineMarkdown(paragraph, orderedListCounter + ". " + line.replaceFirst("^\\d+\\.\\s+", ""), 12);
                    orderedListCounter++;
                }
                // --- 普通段落 ---
                else {
                    parseInlineMarkdown(paragraph, line, 12);
                    orderedListCounter = 1; // 重置有序列表计数
                }
            }

            // ⚠️ 修复：如果文档最后是代码块，循环结束后没有写入，需要处理
            if (inCodeBlock && codeBuffer.length() > 0) {
                writeCodeBlock(document, codeBuffer.toString());
            }

            document.write(out);
        }
    }


    /**
     * 导出 Word 文件（浏览器下载）
     */
    public static void exportWord(String markdown, HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        try (XWPFDocument document = new XWPFDocument();
             OutputStream out = response.getOutputStream()) {

            String[] lines = markdown.split("\n");
            boolean inCodeBlock = false;
            StringBuilder codeBuffer = new StringBuilder();

            for (String line : lines) {
                line = line.replace("\r", "");

                // 代码块
                if (line.startsWith("```")) {
                    if (!inCodeBlock) {
                        inCodeBlock = true;
                        codeBuffer.setLength(0);
                    } else {
                        inCodeBlock = false;
                        writeCodeBlock(document, codeBuffer.toString());
                    }
                    continue;
                }
                if (inCodeBlock) {
                    codeBuffer.append(line).append("\n");
                    continue;
                }

                XWPFParagraph paragraph = document.createParagraph();
                if (line.startsWith("### ")) {
                    createStyledRun(paragraph, line.substring(4), 14, true, false);
                } else if (line.startsWith("## ")) {
                    createStyledRun(paragraph, line.substring(3), 16, true, false);
                } else if (line.startsWith("# ")) {
                    createStyledRun(paragraph, line.substring(2), 20, true, false);
                } else if (line.startsWith("- ")) {
                    parseInlineMarkdown(paragraph, "• " + line.substring(2), 12);
                } else {
                    parseInlineMarkdown(paragraph, line, 12);
                }
            }
            document.write(out);
        }
    }


    /**
     * 解析行内 Markdown（支持 **加粗** 和 *斜体*）
     */
    private static void parseInlineMarkdown(XWPFParagraph paragraph, String text, int fontSize) {
        Pattern pattern = Pattern.compile("(`[^`]+`|\\*\\*[^*]+\\*\\*|\\*[^*]+\\*)");
        Matcher matcher = pattern.matcher(text);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                createStyledRun(paragraph, text.substring(lastEnd, matcher.start()), fontSize, false, false);
            }

            String match = matcher.group();
            if (match.startsWith("`")) {
                createCodeRun(paragraph, match.substring(1, match.length() - 1));
            } else if (match.startsWith("**")) {
                createStyledRun(paragraph, match.substring(2, match.length() - 2), fontSize, true, false);
            } else if (match.startsWith("*")) {
                createStyledRun(paragraph, match.substring(1, match.length() - 1), fontSize, false, true);
            }

            lastEnd = matcher.end();
        }

        if (lastEnd < text.length()) {
            createStyledRun(paragraph, text.substring(lastEnd), fontSize, false, false);
        }
    }

    /**
     * 创建一个代码样式的 run
     */
    private static void createCodeRun(XWPFParagraph paragraph, String text) {
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Consolas");
        run.setFontSize(11);
        run.setText(text);
        run.setColor("333333");
        run.setTextHighlightColor("lightGray"); // 背景高亮
    }

    /**
     * 创建一个 run
     */
    private static void createStyledRun(XWPFParagraph paragraph, String text, int fontSize, boolean bold, boolean italic) {
        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Consolas"); // 中文或普通文本可以用 "Microsoft YaHei"
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setItalic(italic);
        run.setText(text);
    }

    /**
     * 写入代码块
     */
    private static void writeCodeBlock(XWPFDocument document, String code) {
        String[] lines = code.split("\n");

        int lineNumber = 1;
        for (String line : lines) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setSpacingBefore(0);
            paragraph.setSpacingAfter(0);
            paragraph.setIndentationLeft(360); // 缩进 0.5 英寸左右

            XWPFRun run = paragraph.createRun();
            run.setFontFamily("Consolas"); // 等宽字体
            run.setFontSize(11);
            run.setText(line);
            run.setColor("333333");
            run.setTextHighlightColor("lightGray"); // 灰色背景

            // 添加行号（可选）
            // run.setText(lineNumber + "  " + line);
            lineNumber++;
        }
    }
}
