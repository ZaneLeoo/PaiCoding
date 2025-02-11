package com.github.paicoding.forum.web.common.image.rest;

import com.github.paicoding.forum.api.model.vo.ResVo;
import com.github.paicoding.forum.api.model.vo.constants.StatusEnum;
import com.github.paicoding.forum.core.permission.Permission;
import com.github.paicoding.forum.core.permission.UserRole;
import com.github.paicoding.forum.service.image.service.ImageService;
import com.github.paicoding.forum.web.common.image.vo.ImageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 图片服务，要求登录之后才允许操作
 *
 * @author XuYifei
 * @date 2024-07-12
 */
@Permission(role = UserRole.ALL)
@RequestMapping(path = {"/image", "admin/image/", "api/admin/image/",})
@RestController
@Slf4j
public class ImageRestController {

    @Autowired
    private ImageService imageService;

    /**
     * 图片上传
     *
     * @return
     */
//
//    @RequestMapping(path = "upload")
//    public ResVo<ImageVo> upload(HttpServletRequest request) {
//        ImageVo imageVo = new ImageVo();
//        try {
//            String imagePath = imageService.saveImg(request);
//            imageVo.setImagePath(imagePath);
//        } catch (Exception e) {
//            log.error("save upload file error!", e);
//            return ResVo.fail(StatusEnum.UPLOAD_PIC_FAILED);
//        }
//        return ResVo.ok(imageVo);
//    }
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = "D:/Server_Data/Paicoding/Image/" + fileName; // 图片存储路径
        File dest = new File(filePath);
        file.transferTo(dest);
        filePath = "http://localhost:8080/images/" + fileName;
        return "{\"url\": \"" + filePath + "\"}"; // 返回 JSON 格式的 URL
    }

    /**
     * 转存图片
     *
     * @param imgUrl
     * @return
     */
    @RequestMapping(path = "save")
    public ResVo<ImageVo> save(@RequestParam(name = "img", defaultValue = "") String imgUrl) {
        ImageVo imageVo = new ImageVo();
        if (StringUtils.isBlank(imgUrl)) {
            return ResVo.ok(imageVo);
        }

        String url = imageService.saveImg(imgUrl);
        imageVo.setImagePath(url);
        return ResVo.ok(imageVo);
    }
}
