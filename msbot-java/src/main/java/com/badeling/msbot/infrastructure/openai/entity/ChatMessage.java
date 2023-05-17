package com.badeling.msbot.infrastructure.openai.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class ChatMessage {
    private String role;
    private String content;

    public ChatMessage() {

    }

    public ChatMessage(boolean isUser, String content) {
        this.role = isUser ? "user" : "assistant";
        this.content = content;
    }

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String format() {
        if (Objects.equals(role, "system")) {
            return "[背景设定]:" + content;
        } else if (Objects.equals(role, "user")) {
            return "[您]:" + content;
        } else if (Objects.equals(role, "assistant")) {
            return "[助手]:" + content;
        } else {
            return "[??]:" + content;
        }
    }

    public static class JpaConverterJson implements AttributeConverter<List<ChatMessage>, String> {
        private final static ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String convertToDatabaseColumn(List<ChatMessage> o) {
            try {
                return objectMapper.writeValueAsString(o);
            } catch (JsonProcessingException ex) {
                return "[]";
                // or throw an error
            }
        }

        @Override
        public List<ChatMessage> convertToEntityAttribute(String s) {
            try {
                return objectMapper.readValue(s, new TypeReference<List<ChatMessage>>() {
                });
            } catch (IOException ex) {
                // logger.error("Unexpected IOEx decoding json from database: " + dbData);
                return new ArrayList<>();
            }
        }
    }
}
