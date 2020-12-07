package com.util;

import com.github.kevinsawicki.http.HttpRequest;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Download {
    public static final String CAPTCHA_URL = "https://zjy2.icve.com.cn/api/common/VerifyCode/index";



    public static void downImage(CloseableHttpClient client, String imgUrl, String savePath) {
        HttpGet request = new HttpGet(imgUrl);
        // 设置请求超时和传输超时
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(50000).setConnectTimeout(50000).build();

        //伪造请求头
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36");
        request.setConfig(requestConfig);


        try {
            CloseableHttpResponse response = client.execute(request);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                FileUtils.copyInputStreamToFile(in, new File(savePath));
                System.out.println("下载成功:" + imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            request.releaseConnection();

        }
    }

//    public static void main(String[] args) {
//        CloseableHttpClient client =null;
//        try {
//            client =   HttpClients.createDefault();//写循环外
//            Random random = new Random();
//            for (int i = 0; i <9999 ; i++) {
//                downImage(client,"https://zjy2.icve.com.cn/api/common/VerifyCode/index", "E:/image/"+random.nextLong()+".png");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            if(client!=null){
//                try {
//                    client.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }

}
