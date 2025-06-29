package com.lgl;

import com.google.gson.Gson;
import com.lgl.model.ChatRequest;
import com.lgl.model.ChatResponse;
import com.lgl.model.Message;
import com.lgl.util.TypeWriterEffect;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @Author 刘国良
 * @Date 2025/5/8 0:47
 * @Destription
 */

public class DeepSeekClient {

    private static String API_KEY;
    private static String API_URL;
    private static List<Message> messages = new ArrayList<>();

    static {
        Properties properties = new Properties();
        try {
            InputStream is = DeepSeekClient.class.getResourceAsStream("/config.properties");
            properties.load(is);
            API_KEY = properties.getProperty("key");
            API_URL = properties.getProperty("url");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public static void ask(String content) {
        // 创建消息列表
//        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", content));
        // 构建请求体
        ChatRequest requestBody = new ChatRequest(
                "deepseek-chat",  // 模型名称，根据文档调整
                messages,
                0.7,    // temperature
                1000    // max_tokens
        );
        System.out.println(">>>正在提交问题...");
        long startTime = System.currentTimeMillis();
        // 发送请求
        String response = sendRequest(requestBody);
        messages.add(new Message("assistant", response));
        long endTime = System.currentTimeMillis();
        System.out.println("思考用时："+(endTime - startTime)/1000+"秒");
//        System.out.println("响应内容: " + response);
        TypeWriterEffect.printword(response,20);
    }

    private static String sendRequest (ChatRequest requestBody) {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        //将 ChatRequest 对象中封装的数据转为 JSON 格式
        String requestBodyJson = gson.toJson(requestBody);

        System.out.println("请求体：");
        System.out.println(requestBodyJson);

        try {
            //构建请求对象 并指定请求头内容格式及身份验证的key
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    //将JSON格式的字符串封装为 BodyPublishers 对象
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                    .build();  //构建请求对象

            System.out.println(">>>已提交问题，正在思考中....");
            // 发送请求并获取响应对象
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            //如果响应状态码为成功 200
        if (response.statusCode() == 200) {
//            System.out.println("响应体：");
//            System.out.println(response.body());
            // 解析响应 把响应体中的json字符串转为 ChatResponse 对象
            ChatResponse chatResponse = gson.fromJson(response.body(), ChatResponse.class);
            //按 JSON 格式的方式 从自定义的ChatResponse 对象中逐级取出最终的响应对象
            return chatResponse.getChoices().get(0).getMessage().getContent();
        } else {
            return "请求失败，状态码: " + response.statusCode() + "，响应: " + response.body();
        }
        } catch (Exception e) {
            e.printStackTrace();
            return "请求异常: " + e.getMessage();
        }
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        System.out.println("*** 我是 DeepSeek ，很高兴见到您 ****");
        while (true) {
            System.out.println("---请说您问题：---");
            String question = scanner.next();
            if ("bye".equals(question)) {
                break;
            }
            ask(question);
            System.out.println();
        }
        System.out.println("拜拜，欢迎下次使用！");
    }
}

