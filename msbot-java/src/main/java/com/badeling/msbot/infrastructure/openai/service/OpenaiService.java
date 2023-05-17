package com.badeling.msbot.infrastructure.openai.service;

import com.badeling.msbot.infrastructure.dao.entity.OpenaiChat;
import com.badeling.msbot.infrastructure.dao.repository.OpenaiChatRepository;
import com.badeling.msbot.infrastructure.openai.entity.ChatMessage;
import com.badeling.msbot.infrastructure.openai.entity.OpenaiChatCompletionsRequest;
import com.badeling.msbot.infrastructure.openai.repository.OpenaiApiRepository;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenaiService {

    private final Map<Long, OpenaiChat> userChattingMap;
    private final OpenaiChatRepository openaiChatRepository;
    private final OpenaiApiRepository openaiApiRepository;

    private final ChatMessage.JpaConverterJson chatMessageListConverter;

    public OpenaiService(
            final OpenaiChatRepository openaiChatRepository,
            final OpenaiApiRepository openaiApiRepository
    ) {
        this.userChattingMap = new HashMap<>();
        this.openaiChatRepository = openaiChatRepository;
        this.openaiApiRepository = openaiApiRepository;
        this.chatMessageListConverter = new ChatMessage.JpaConverterJson();
    }

    /**
     * 开始聊天. 展示之前对话
     *
     * @param qq
     * @return
     */
    public OpenaiChat startChat(Long qq) {
        //内存中已存在
        if (userChattingMap.containsKey(qq)) {
            return userChattingMap.get(qq);
        }

        //数据库里已存在
        var last = openaiChatRepository.findByQQ(qq);
        if (last.isPresent()) {
            userChattingMap.put(qq, last.get());
            return last.get();
        }

        //新对话
        var list = new ArrayList<ChatMessage>();
        list.add(new ChatMessage("system", "You are a helpful assistant."));

        var chat = new OpenaiChat();
        chat.setQq(qq);
        chat.setChat(list);
        chat.setIn_valid(OpenaiChat.IN_VALID_ENABLE);
        openaiChatRepository.save(chat);
        return chat;
    }

    public void endStart(Long qq){
        //内存中已存在
        if (!userChattingMap.containsKey(qq)) {
            return;
        }

        var last = userChattingMap.get(qq);


        openaiChatRepository.disableChat(last.getId());
        userChattingMap.remove(qq);
    }

    public boolean canChat(Long qq){
        return userChattingMap.containsKey(qq);
    }

    @Nullable
    public String chat(Long qq, String content) {
        if (!userChattingMap.containsKey(qq)) {
            return null;
        }

        var last = userChattingMap.get(qq);
        // 对话前文
        List<ChatMessage> chatMessages = last.getChat();
        // api 请求
        OpenaiChatCompletionsRequest request = new OpenaiChatCompletionsRequest(chatMessages);


        // 用户发送内容
        chatMessages.add(new ChatMessage(true, content));

        //请求
        var response = openaiApiRepository.chat(request);
        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            var retVal = response.getChoices().get(0).getMessage();
            chatMessages.add(retVal);

            //保存
            var id = last.getId();
            var c = chatMessageListConverter.convertToDatabaseColumn(chatMessages);
            openaiChatRepository.updateChat(id, c);

            return retVal.getContent();
        }
        return null;
    }

}
