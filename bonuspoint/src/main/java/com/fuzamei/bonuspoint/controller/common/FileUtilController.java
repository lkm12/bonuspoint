package com.fuzamei.bonuspoint.controller.common;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.common.bean.FastDFSClient;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图像上出和下载Controller
 *
 * @author liumeng
 * @create 2018年6月21日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/")
public class FileUtilController {

    @Value("${fdfs.web-server-url}")
    private String fdfsRequestUrl;

    private final FastDFSClient fastDFSClient;

    @Autowired
    public FileUtilController(FastDFSClient fastDFSClient) {
        this.fastDFSClient = fastDFSClient;
    }

    /**
     * 上传图像
     *
     * @param token  用户token
     * @param images 上传文件
     * @return
     */
    @PostMapping("/images/upload")
    public ResponseVO uploadImage(@RequestAttribute("token") Token token, MultipartFile[] images) {
        log.info("上传图像文件");
        //上传文件
        StringBuffer srcUrls = new StringBuffer();
        if (images != null && images.length > 0) {
            String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
            //判断文件格式是否正确
            for (MultipartFile image : images) {
                String name = image.getOriginalFilename();
                if (!name.matches(reg)) {
                    return new ResponseVO(CommonResponseEnum.IMAGE_FOMMAT_NOT_RIGHT);
                }
            }
            //上传图像
            for (MultipartFile image : images) {
                try {
                    String url = fastDFSClient.uploadFile(image);
                    if (url != null) {
                        if (srcUrls.length() != 0) {
                            url = "," + url;
                        }
                        srcUrls.append(url);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    log.info("上传图像异常", e);
                    // 删除原来上传的图像，不一定成功
                    if (srcUrls != null && srcUrls.length() > 0) {
                        String[] urls = srcUrls.toString().split(",");
                        for (String url : urls) {
                            fastDFSClient.deleteFile(url);
                        }

                    }
                    return new ResponseVO(CommonResponseEnum.FAILURE);
                }

            }
        } else {
            return new ResponseVO(CommonResponseEnum.IMAGE_IS_EMPTY);
        }
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, (Object) srcUrls.toString());
    }

    /**
     * 上传文件
     * @param token token
     * @param file file
     * @return
     */
    @PostMapping("/platform/apk/upload")
    public ResponseVO uploadAPK(@RequestAttribute("token") Token token, MultipartFile file) {
        log.info("上传APK安装包");
        String reg = ".+apk$";
        String name = file.getOriginalFilename();
        if (!name.matches(reg)) {
            return new ResponseVO(CommonResponseEnum.APK_FORMAT_ERROR);
        }
        try {
            String url = fastDFSClient.uploadFile(file);
            if (url == null) {
                throw new Exception("文件上传错误");
            }
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, (Object) url.toString());
        } catch (Exception e) {
            log.info("文件上传异常！");
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }


    /**
     * 获取文件请求url
     * @return
     */
    @GetMapping("/server/file/url")
    public ResponseVO getFileRequestUrl() {
        log.info("获取文件服务器请求路径");
        return new ResponseVO(CommonResponseEnum.SUCCESS, (Object) this.fdfsRequestUrl);
    }

}
