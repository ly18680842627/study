package com.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理图片降噪
 */
public class ProcessingCodeImage {

    /**
     * 去噪点，使用连通域大小来判断
     *
     * @param originalCaptcha 原始的验证码图片
     * @param areaSizeFilter  连通域小于等于此大小的将被过滤掉
     * @return
     */
    public static BufferedImage noiseClean(BufferedImage originalCaptcha, int areaSizeFilter) {

        // 会有一些干扰边，把边缘部分切割丢掉
        int edgeDropWidth = 15;
        BufferedImage captcha = originalCaptcha.getSubimage(edgeDropWidth / 2, edgeDropWidth / 2,  //
                originalCaptcha.getWidth() - edgeDropWidth, originalCaptcha.getHeight() - edgeDropWidth);

        int w = captcha.getWidth();
        int h = captcha.getHeight();
        int[][] book = new int[w][h];

        // 连通域最大的色块将被认为是背景色，这样实现了自动识别背景色
        Map<Integer, Integer> flagAreaSizeMap = new HashMap<>();
        int currentFlag = 1;
        int maxAreaSizeFlag = currentFlag;
        int maxAreaSizeColor = 0XFFFFFFFF;

        // 标记
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {

                if (book[i][j] != 0) {
                    continue;
                }

                book[i][j] = currentFlag;
                int currentColor = captcha.getRGB(i, j);
                int areaSize = waterFlow(captcha, book, i, j, currentColor, currentFlag);

                if (areaSize > flagAreaSizeMap.getOrDefault(maxAreaSizeFlag, 0)) {
                    maxAreaSizeFlag = currentFlag;
                    maxAreaSizeColor = currentColor;
                }

                flagAreaSizeMap.put(currentFlag, areaSize);
                currentFlag++;
            }
        }

        // 复制
        BufferedImage resultImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int currentColor = captcha.getRGB(i, j);
                if (book[i][j] == maxAreaSizeFlag //
                        || (currentColor & 0XFFFFFF) == (maxAreaSizeColor & 0XFFFFFF) //
                        || flagAreaSizeMap.get(book[i][j]) <= areaSizeFilter) {
                    resultImage.setRGB(i, j, 0XFFFFFFFF);
                } else {
                    resultImage.setRGB(i, j, currentColor);
                }
            }
        }
        return resultImage;
    }

    /**
     * 将图像抽象为颜色矩阵
     *
     * @param img
     * @param book
     * @param x
     * @param y
     * @param color
     * @param flag
     * @return
     */
    private static int waterFlow(BufferedImage img, int[][] book, int x, int y, int color, int flag) {

        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            return 0;
        }

        // 这个1统计的是当前点
        int areaSize = 1;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nextX = x + i;
                int nextY = y + j;

                if (nextX < 0 || nextX >= img.getWidth() || nextY < 0 || nextY >= img.getHeight()) {
                    continue;
                }

                // 如果这一点没有被访问过，并且颜色相同
                //              if (book[nextX][nextY] == 0 && isSimilar(img.getRGB(nextX, nextY), color, 0)) {
                if (book[nextX][nextY] == 0 && (img.getRGB(nextX, nextY) & 0XFFFFFF) == (color & 0XFFFFFF)) {
                    book[nextX][nextY] = flag;
                    areaSize += waterFlow(img, book, nextX, nextY, color, flag);
                }

            }
        }

        return areaSize;
    }


    /**
     * 切割字符
     *
     * @param img
     * @return
     */
    public static List<BufferedImage> mattingCharacter(BufferedImage img) {
        List<BufferedImage> list = new ArrayList<>();

        int w = img.getWidth();
        int h = img.getHeight();

        boolean lastColumnIsBlack = true;
        int beginColumn = -1;

        for (int i = 0; i < w; i++) {

            boolean currentColumnIsBlack = true;
            for (int j = 0; j < h; j++) {
                if ((img.getRGB(i, j) & 0XFFFFFF) != 0XFFFFFF) {
                    currentColumnIsBlack = false;
                }
            }

            // 进入字符区域
            if (lastColumnIsBlack && !currentColumnIsBlack) {
                beginColumn = i;
            } else if (!lastColumnIsBlack && currentColumnIsBlack) {
                // 离开字符区域
                BufferedImage charImage = img.getSubimage(beginColumn, 0, i - beginColumn, h);
                BufferedImage trimCharImage = trimUpAndDown(charImage);
                list.add(trimCharImage);
            }

            lastColumnIsBlack = currentColumnIsBlack;

        }

        return list;
    }

    private static BufferedImage trimUpAndDown(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        // 计算上方空白
        int upBeginLine = -1;
        for (int i = 0; i < h; i++) {

            boolean currentColumnIsBlack = true;
            for (int j = 0; j < w; j++) {
                if ((img.getRGB(j, i) & 0XFFFFFF) != 0XFFFFFF) {
                    currentColumnIsBlack = false;
                }
            }

            if (!currentColumnIsBlack) {
                upBeginLine = i;
                break;
            }

        }

        // 计算下方空白
        int downBeginLine = -1;
        for (int i = h - 1; i >= 0; i--) {

            boolean currentColumnIsBlack = true;
            for (int j = 0; j < w; j++) {
                if ((img.getRGB(j, i) & 0XFFFFFF) != 0XFFFFFF) {
                    currentColumnIsBlack = false;
                }
            }

            if (!currentColumnIsBlack) {
                downBeginLine = i;
                break;
            }
        }

        return img.getSubimage(0, upBeginLine, w, downBeginLine - upBeginLine + 1);
    }

    /**
     * 计算图像的哈希值，即将图片内容压缩为一个整数
     * <p>
     * NOTE: 适用于小图像
     *
     * @param img
     * @return
     */
    public static int imgHashCode(BufferedImage img) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                sb.append(i).append("|").append(j).append("|").append(img.getRGB(i, j) & 0XFFFFFF).append("|");
            }
        }
        return sb.toString().hashCode();
    }
//
//    public static void main(String[] args) throws IOException, InterruptedException {
//        String imgPath2 = "G:\\project\\study\\src\\main\\resources\\image\\784924-20180127022055662-1988958641.png";
//        BufferedImage image = ImageIO.read(new FileInputStream(imgPath2));
//        BufferedImage next = noiseClean(image,1);
//        File outputfile  = new File("G:\\project\\study\\src\\main\\resources\\image\\save.png");
//        ImageIO.write(next,"png",outputfile);
//        Thread.sleep(50000);
//        String imgPath = "G:\\project\\study\\src\\main\\resources\\image\\save.png";
//        BufferedImage image1 = ImageIO.read(new FileInputStream(imgPath));
//
//
//        List<BufferedImage> bufferedImageList = mattingCharacter(image1);
//        int i = 0;
//        for (BufferedImage bufferedImage:bufferedImageList) {
//            i= 1 + i;
//            File outputfile1  = new File("G:\\project\\study\\src\\main\\resources\\image\\"+i+".png");
//            ImageIO.write(bufferedImage,"png",outputfile1);
//        }


    //}
}
