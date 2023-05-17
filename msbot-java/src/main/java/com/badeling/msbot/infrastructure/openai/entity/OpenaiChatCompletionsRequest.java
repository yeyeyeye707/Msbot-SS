package com.badeling.msbot.infrastructure.openai.entity;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class OpenaiChatCompletionsRequest {

    public OpenaiChatCompletionsRequest() {
        model = "gpt-3.5-turbo";
        messages = new ArrayList<>();
    }

    public OpenaiChatCompletionsRequest(List<ChatMessage> messages) {
        model = "gpt-3.5-turbo";
        this.messages = messages;
    }

    private String model;

    private List<ChatMessage> messages;

    public void addMessage(boolean isUser, String content) {
        messages.add(new ChatMessage(isUser ? "user" : "assistant", content));
    }
}
