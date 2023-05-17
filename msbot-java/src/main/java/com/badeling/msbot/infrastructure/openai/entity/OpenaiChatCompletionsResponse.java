package com.badeling.msbot.infrastructure.openai.entity;

import lombok.Data;

import java.util.List;

@Data
public class OpenaiChatCompletionsResponse {
    private String id;

    private String object;

    private Long created;

    private String model;

    private Usage usage;

    private List<Choice> choices;

    @Data
    public static class Usage {

        private Integer prompt_tokens;

        private Integer completion_tokens;

        private Integer total_tokens;
    }

    @Data
    public static class Choice {

        private ChatMessage message;

        private String finish_reason;

        private Integer index;
    }
}
