package com.badeling.msbot.infrastructure.maplegg.service;

import com.badeling.msbot.infrastructure.dao.repository.LevelExpRepository;
import com.badeling.msbot.infrastructure.maplegg.entity.RankResponse;
import com.badeling.msbot.infrastructure.maplegg.exception.HttpAccessException;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class RankService {

    @Autowired
    private LevelExpRepository levelExpRepository;


    public String getRank(String raw_message) {
        String characterName = raw_message.replace(MsbotConst.botName, "")
                .replace("联盟", "")
                .replace(" ", "");

        RankResponse response;
        try {
            response = requestDataResponse(characterName);
        } catch (HttpAccessException e) {
            return characterName + "查询角色不存在" + e.getMessage();
        }

//        return response.getDataString(mvpImageService);
        if (response == null || response.getCharacterData() == null) {
            return "没找到呀";
        }

        return response.getCharacterData().getCharacterString(levelExpRepository);
    }

    private static final Charset charset = StandardCharsets.UTF_8;

    public static RankResponse requestDataResponse(String characterName) throws HttpAccessException {

        String urlString = "https://api.maplestory.gg/v2/public/character/gms/" + characterName;
        try {
//            logger.debug("Requesting: {}", urlString);
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);
//            connection.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
//            connection.setRequestMethod(method == Method.POST ? "POST" : "GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

//            if (body != null) {
//                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), charset);
////                logger.debug("Sending payload: {}", body);
//                writer.write(body);
//                writer.close();
//            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ObjectMapper om = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return om.readValue(new InputStreamReader(connection.getInputStream(), charset), RankResponse.class);
            } else {
//                logger.error("Bad response code: {}", responseCode);
                throw new HttpAccessException("Bad response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            throw new HttpAccessException("Malformed url", e);
        } catch (JsonProcessingException parseException) {
            throw new HttpAccessException("Failed to parse response as JSON", parseException);
        } catch (IOException e) {
            throw new HttpAccessException("IOException", e);
        }
    }
}

