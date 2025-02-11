package com.github.paicoding.forum.core.net;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ByteArrayResource;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求工具类（只保留文件上传功能）
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
public class HttpRequestHelper {

    /**
     * 文件上传
     *
     * @param url       上传url
     * @param paramName 参数名
     * @param fileName  上传的文件名
     * @param bytes     上传文件流
     * @return 响应内容
     */
    public static String upload(String url, String paramName, String fileName, byte[] bytes) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 设置请求体，注意是 LinkedMultiValueMap
        ByteArrayResource fileSystemResource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add(paramName, fileSystemResource);

        // 用 HttpEntity 封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        // 使用默认的 RestTemplate 发送请求
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> res = restTemplate.postForEntity(url, files, String.class);

        return res.getBody();
    }
}
