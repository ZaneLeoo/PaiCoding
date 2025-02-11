package com.github.paicoding.forum.service.image.oss.impl;

import com.github.paicoding.forum.core.config.ImageProperties;
import com.github.paicoding.forum.core.net.HttpRequestHelper;
import com.github.paicoding.forum.core.util.JsonUtil;
import com.github.paicoding.forum.service.image.oss.ImageUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于http的文件上传
 * @author XuYifei
 * @date 2024-07-12
 */
@Slf4j
@Component
@ConditionalOnExpression(value = "#{'rest'.equals(environment.getProperty('image.oss.type'))}")
public class RestOssWrapper implements ImageUploader {
    @Autowired
    private ImageProperties properties;

    @Override
    public String upload(InputStream input, String fileType) {
        try {
            // 将输入流转化为字节数组
            byte[] bytes = StreamUtils.copyToByteArray(input);
            // 上传图片
            String res = HttpRequestHelper.
                    upload(properties.getOss().getEndpoint(), "image", "img." + fileType, bytes);
            // 解析返回结果并获取图片路径
            HashMap<?, ?> map = JsonUtil.toObj(res, HashMap.class);
            return (String) ((Map<?, ?>) map.get("result")).get("imagePath");
        } catch (Exception e) {
            log.error("upload image error response! uri:{}", properties.getOss().getEndpoint(), e);
            return null;
        }
    }


    @Override
    public boolean uploadIgnore(String fileUrl) {
        if (StringUtils.isNotBlank(properties.getOss().getHost()) && fileUrl.startsWith(properties.getOss().getHost())) {
            return true;
        }
        return !fileUrl.startsWith("http");
    }
}
