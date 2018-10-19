package com.fuzamei.bonuspoint.util;

import com.fuzamei.common.bean.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 图片的压缩
 * Created by 18519 on 2018/4/28.
 */
@Component
public class ImageUtil {

    private final FastDFSClient fastDFSClient;

    @Autowired
    public ImageUtil(FastDFSClient fastDFSClient) {
        this.fastDFSClient = fastDFSClient;
    }

    public String  reduceImg(MultipartFile fi, int widthdist, int heightdist, Float rate) {
        try {

            // 如果比例不为空则说明是按比例压缩
            if (rate != null && rate > 0) {
                //获得源图片的宽高存入数组中
                int[] results = getImgWidthHeight(fi);
                if (results == null || results[0] == 0 || results[1] == 0) {
                    return null;
                } else {
                    //按比例缩放或扩大图片大小，将浮点型转为整型
                    widthdist = (int) (results[0] * rate);
                    heightdist = (int) (results[1] * rate);
                }
            }
            // 开始读取文件并进行压缩
            Image src = ImageIO.read(fi.getInputStream());

            // 构造一个类型为预定义图像类型之一的 BufferedImage
            BufferedImage tag = new BufferedImage((int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB);

            //绘制图像  getScaledInstance表示创建此图像的缩放版本，返回一个新的缩放版本Image,按指定的width,height呈现图像
            //Image.SCALE_SMOOTH,选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            tag.getGraphics().drawImage(src.getScaledInstance(widthdist, heightdist, Image.SCALE_SMOOTH), 0, 0, null);
            String imageFileName = fi.getOriginalFilename();
            String endType = imageFileName.split("\\.")[1];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(tag, endType ,outputStream);
            byte[] by = outputStream.toByteArray();
            return fastDFSClient.uploadFile(by, imageFileName);
        } catch (Exception ef) {
            ef.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片宽度和高度
     *
     * @param
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(MultipartFile file) {

        BufferedImage src = null;
        int result[] = {0, 0};
        try {

            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(file.getInputStream());
            result[0] = src.getWidth(null); // 得到源图片宽
            result[1] = src.getHeight(null);// 得到源图片高

        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }
}
