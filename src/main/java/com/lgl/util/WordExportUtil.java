package com.lgl.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import org.ddr.poi.html.HtmlRenderPolicy;
import org.springframework.core.io.ClassPathResource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordExportUtil {

    public static byte[] exportKnowledgeToWord(String title, String summary, String content) throws IOException, ClassNotFoundException {

        // html渲染插件
        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
        // 注册html解析插件
        Configure configure = Configure.builder().bind("content", htmlRenderPolicy).build();
        // 准备模板数据
        Map<String, Object> data = new HashMap<>();
        data.put("title",  title);
        data.put("summary",  summary);
//        data.put("content",  "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; color: #333; max-width: 800px; margin: 0 auto;\">    <h1 style=\"color: #2c3e50; border-bottom: 2px solid #e74c3c; padding-bottom: 10px;\">人工智能技术前沿报告</h1>        <p style=\"font-size: 16px; line-height: 1.6;\">        <span style=\"font-weight: bold; color: #2980b9;\">生成式AI</span>正在重塑内容创作范式。根据<span style=\"background-color: #fffde7;\">Gartner 2025年预测</span>，到2026年，<u>30%的企业营销材料</u>将通过AI生成。    </p>        <blockquote style=\"border-left: 4px solid #3498db; padding-left: 15px; margin-left: 0; font-style: italic;\">        \"多模态大模型已突破单一数据类型的限制，实现<span style=\"color: #c0392b;\">文本→图像→视频</span>的跨模态生成\" —— <span style=\"font-size: 14px;\">MIT Tech Review</span>    </blockquote>        <table style=\"width: 100%; border-collapse: collapse; margin: 20px 0;\">        <thead>            <tr style=\"background-color: #34495e; color: white;\">                <th style=\"padding: 12px; text-align: left;\">技术领域</th>                <th style=\"padding: 12px; text-align: center;\">成熟度</th>                <th style=\"padding: 12px; text-align: right;\">投资热度</th>            </tr>        </thead>        <tbody>            <tr style=\"border-bottom: 1px solid #ddd;\">                <td style=\"padding: 10px;\">LLM（大语言模型）</td>                <td style=\"text-align: center; padding: 10px;\">★★★★☆</td>                <td style=\"text-align: right; padding: 10px;\">$48B</td>            </tr>            <tr style=\"background-color: #f9f9f9;\">                <td style=\"padding: 10px;\">AIGC（生成式AI）</td>                <td style=\"text-align: center; padding: 10px;\">★★★☆☆</td>                <td style=\"text-align: right; padding: 10px;\">$23B</td>            </tr>        </tbody>    </table>        <div style=\"background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0;\">        <h3 style=\"margin-top: 0; color: #27ae60;\">关键技术挑战：</h3>        <ul style=\"padding-left: 25px;\">            <li>幻觉（Hallucination）问题</li>            <li>多模态对齐（Alignment）</li>            <li style=\"text-decoration: line-through;\">算力成本过高（2024年后显著下降）</li>        </ul>    </div>        <p style=\"text-align: center;\">        <img src=\"https://example.com/ai-trend.png\"  alt=\"AI技术趋势图\" style=\"max-width: 90%; border: 1px solid #ddd; box-shadow: 0 2px 4px rgba(0,0,0,0.1);\">        <br>        <span style=\"font-size: 12px; color: #7f8c8d;\">▲ 2023-2025年生成式AI技术成熟度曲线</span>    </p>        <div style=\"font-size: 14px; color: #95a5a6; text-align: right; margin-top: 30px;\">        报告生成时间：2025-05-17 |         <span style=\"font-family: monospace;\">[系统自动生成]</span>     </div></div>");
        data.put("content", "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        .header {\n" +
                "            border-bottom: 1px solid #ddd;\n" +
                "            padding-bottom: 10px;\n" +
                "            margin-bottom: 20px;\n" +
                "            text-align: center;\n" +
                "            font-family: Arial, sans-serif;\n" +
                "        }\n" +
                "        .title {\n" +
                "            font-size: 24px;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 15px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .subtitle {\n" +
                "            font-size: 16px;\n" +
                "            color: #555;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            font-family: \"Times New Roman\", serif;\n" +
                "            font-size: 12pt;\n" +
                "            line-height: 1.5;\n" +
                "            text-indent: 2em;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            border-top: 1px solid #ddd;\n" +
                "            margin-top: 20px;\n" +
                "            padding-top: 10px;\n" +
                "            font-size: 10pt;\n" +
                "            text-align: center;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"header\">\n" +
                "        <div style=\"font-size: 10pt; text-align: left;\">机密文档 - 2025年5月30日</div>\n" +
                "        <div style=\"font-size: 14pt; font-weight: bold;\">XX公司项目计划书</div>\n" +
                "    </div>\n" +
                " \n" +
                "    <div class=\"title\">数字化转型项目实施方案</div>\n" +
                "    <div class=\"subtitle\">版本：V3.2 | 编制日期：2025-05-30</div>\n" +
                " \n" +
                "    <div class=\"content\">\n" +
                "        <h2>一、项目背景</h2>\n" +
                "        <p>随着人工智能技术的快速发展，公司亟需通过数字化转型提升运营效率。根据Gartner 2025年最新报告显示，全球85%的企业已启动数字化升级项目。</p>\n" +
                " \n" +
                "        <h2>二、实施目标</h2>\n" +
                "        <ol>\n" +
                "            <li><strong>业务流程自动化</strong>：实现采购、生产、销售全流程自动化</li>\n" +
                "            <li><strong>数据中台建设</strong>：构建统一数据仓库，支持实时分析</li>\n" +
                "            <li><strong>AI赋能</strong>：部署智能客服和预测性维护系统</li>\n" +
                "        </ol>\n" +
                " \n" +
                "        <h2>三、技术方案</h2>\n" +
                "        <table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" style=\"width: 100%;\">\n" +
                "            <tr>\n" +
                "                <th>模块</th>\n" +
                "                <th>技术栈</th>\n" +
                "                <th>负责人</th>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>前端</td>\n" +
                "                <td>React 22 + WebAssembly</td>\n" +
                "                <td>张明</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>后端</td>\n" +
                "                <td>Java 21 + Spring Boot 4.0</td>\n" +
                "                <td>李华</td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                " \n" +
                "        <h2>四、项目里程碑</h2>\n" +
                "        <ul>\n" +
                "            <li><u>2025-06-15</u>：完成需求分析</li>\n" +
                "            <li><u>2025-07-30</u>：核心模块开发</li>\n" +
                "            <li><u>2025-09-01</u>：系统上线</li>\n" +
                "        </ul>\n" +
                "    </div> \n" +
                " \n" +
                "    <div class=\"footer\">\n" +
                "        第1页 共3页 | XX公司版权所有 © 2025 | 内部使用 \n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");

        // 加载模板文件
        ClassPathResource resource = new ClassPathResource("templates/knowledge_template.docx");
        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream(),  configure).render(data);

        // 生成Word字节数组
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        template.write(out);
        out.close();
        template.close();

        return out.toByteArray();
    }

}