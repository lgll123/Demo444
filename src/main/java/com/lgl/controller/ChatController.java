package com.lgl.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api")
public class ChatController {

    // 存储正在进行的SSE会话和取消标志 
    private final Map<String, SessionData> activeSessions = new ConcurrentHashMap<>();

    // 问答接口 
    @PostMapping("/ask")
    public SseEmitter askQuestion(@RequestBody QuestionRequest request) {
        String sessionId = request.getSessionId();
        SseEmitter emitter = new SseEmitter(60_000L); // 60秒超时 

        // 创建会话数据对象 
        AtomicBoolean isCancelled = new AtomicBoolean(false);
        SessionData sessionData = new SessionData(emitter, isCancelled);
        activeSessions.put(sessionId,  sessionData);

        // 设置完成和超时回调 
        emitter.onCompletion(()  -> {
            isCancelled.set(true);
            activeSessions.remove(sessionId);
        });
        emitter.onTimeout(()  -> {
            isCancelled.set(true);
            emitter.complete();
            activeSessions.remove(sessionId);
        });

        // 模拟调用大模型并流式返回 
        new Thread(() -> {
            try {
                for (int i = 0; i < 10000; i++) { // 模拟大模型分块返回
                    if (isCancelled.get())  { // 检查是否被取消 
                        break;
                    }
                    Thread.sleep(200);  // 模拟处理延迟 
                    emitter.send(SseEmitter.event()
                            .id(String.valueOf(i))
                            .data("Chunk " + i + " of answer")
                            .name("message"));
                }
                emitter.complete();
            } catch (Exception e) {
                if (!isCancelled.get())  { // 如果不是因为取消导致的异常 
                    emitter.completeWithError(e);
                }
            } finally {
                activeSessions.remove(sessionId);
            }
        }).start();

        return emitter;
    }

    // 停止接口 
    @PostMapping("/stop")
    public DeferredResult<ResponseEntity<?>> stopGeneration(@RequestBody StopRequest request) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        String sessionId = request.getSessionId();

        SessionData sessionData = activeSessions.get(sessionId);
        if (sessionData != null) {
            // 设置取消标志 
            sessionData.getIsCancelled().set(true);
            // 完成当前SSE流 
            sessionData.getEmitter().complete();
            activeSessions.remove(sessionId);
            result.setResult(ResponseEntity.ok().build());
        } else {
            result.setResult(ResponseEntity.notFound().build());
        }

        return result;
    }

    // 会话数据类 
    private static class SessionData {
        private final SseEmitter emitter;
        private final AtomicBoolean isCancelled;

        public SessionData(SseEmitter emitter, AtomicBoolean isCancelled) {
            this.emitter  = emitter;
            this.isCancelled  = isCancelled;
        }

        public SseEmitter getEmitter() {
            return emitter;
        }

        public AtomicBoolean getIsCancelled() {
            return isCancelled;
        }
    }

    // 请求体类 
    public static class QuestionRequest {
        private String sessionId;
        private String question;
        // getters and setters

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }

    public static class StopRequest {
        private String sessionId;
        // getters and setters

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}