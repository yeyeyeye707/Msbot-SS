package com.badeling.msbot.infrastructure.dao.entity;

import com.badeling.msbot.infrastructure.openai.entity.ChatMessage;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class OpenaiChat {
    public static Byte IN_VALID_ENABLE = 1;
    public static Byte IN_VALID_DISABLE = 0;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long qq;

    @Convert(converter=ChatMessage.JpaConverterJson.class)
    private List<ChatMessage> chat;

    private Byte in_valid;
}
