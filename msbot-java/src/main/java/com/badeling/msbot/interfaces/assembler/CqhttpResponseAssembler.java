package com.badeling.msbot.interfaces.assembler;

//import com.alibaba.fastjson.JSON;
import com.badeling.msbot.application.entity.MessageResult;
import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.interfaces.dto.CqhttpResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CqhttpResponseAssembler {
    private ObjectMapper om = new ObjectMapper();
    public CqhttpResponseDto notice(NoticeResult result){
        CqhttpResponseDto response =  new CqhttpResponseDto();
        if(result != null) {
            response.setJson(result.getJsonStr());
        }
        return  response;
    }

    public CqhttpResponseDto message(MessageResult result){
        CqhttpResponseDto response =  new CqhttpResponseDto();
        if(result != null) {
//            System.out.println(result.toString());
            try{
                String jsonStr = om.writeValueAsString(result);
                response.setJson(jsonStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

//            String jsonStr = JSON.toJSONString(result);
//            response.setJson(jsonStr);
        }
        return  response;
    }
}
