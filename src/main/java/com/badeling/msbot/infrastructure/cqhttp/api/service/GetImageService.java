package com.badeling.msbot.infrastructure.cqhttp.api.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//https://docs.go-cqhttp.org/api/#获取图片信息
@Service
public class GetImageService {
    RestTemplate restTemplate = new RestTemplate();

    public GetImageResult getImage(String file){
        GetImageRequest request = new GetImageRequest();
        request.setFile(file);
        GetImageResult result = restTemplate.postForObject("http://127.0.0.1:5700/get_image", request, GetImageResult.class);
//        System.err.println(result.toString());
        return result;
    }
}
