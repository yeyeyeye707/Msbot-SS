package com.badeling.msbot.infrastructure.openai.repository;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.openai.entity.OpenaiChatCompletionsRequest;
import com.badeling.msbot.infrastructure.openai.entity.OpenaiChatCompletionsResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class OpenaiApiRepository {
    private final Proxy proxy;
    private final String openaiAuth;

    private final ObjectMapper objectMapper;

    public OpenaiApiRepository(
            ConstRepository constRepository,
            ObjectMapper objectMapper
    ) {
        //创建代理虽然是https也是Type.HTTP
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(constRepository.getProxyHostname(), constRepository.getProxyPort()));
        openaiAuth = "Bearer " + constRepository.getOpenaiAuth();
        this.objectMapper = objectMapper;
    }

    //https://platform.openai.com/docs/guides/chat
    @Nullable
    public OpenaiChatCompletionsResponse chat(OpenaiChatCompletionsRequest request) {

//        //对话前文
//        var request = new OpenaiChatCompletionsRequest();
//        request.addMessage(true, "Who won the world series in 2020?");
        try {
            String urlString = "https://api.openai.com/v1/chat/completions";
            var url = new URL(urlString);
//            SSLContext sc = SSLContext.getInstance("SSL");
//            // 指定信任https
//            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            //设置代理
            var connection = (HttpsURLConnection) url.openConnection(proxy);

            connection.setRequestProperty("Authorization", openaiAuth);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String body = objectMapper.writeValueAsString(request);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            log.debug("Sending payload: {}", body);
            writer.write(body);
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                ObjectMapper om = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return om.readValue(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), OpenaiChatCompletionsResponse.class);
            } else {
                log.warn("http failed with openai, {}", responseCode);
            }
        } catch (Exception ex) {
            log.warn("error with openai", ex);
        }
        return null;
    }
}

//    curl https://api.openai.com/v1/models \
//        -H "Authorization: Bearer $OPENAI_API_KEY"


//        curl https://api.openai.com/v1/chat/completions
// -H "Content-Type: application/json"
// -H "Authorization: Bearer $OPENAI_API_KEY"
// -d '{  "model": "gpt-3.5-turbo", "messages": [{"role": "user", "content": "Hello!"}] }'
