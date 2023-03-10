package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://docs.go-cqhttp.org/api/#获取图片信息
@Service
public class GetImageService {
    private final RestTemplate restTemplate;

    private final String url;

    public GetImageService(
            final ConstRepository constRepository
    ) {
        restTemplate = new RestTemplate();
        url = constRepository.getFrontEndUrl() + "/get_image";
    }

    public GetImageResult getImage(String file) {
        GetImageRequest request = new GetImageRequest();
        request.setFile(file);
        GetImageResult result = restTemplate.postForObject(url, request, GetImageResult.class);
//        System.err.println(result.toString());
        return result;
    }
}
