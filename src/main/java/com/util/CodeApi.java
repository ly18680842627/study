package com.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 超级鹰api调用获取验证码
 */
public class CodeApi {

    /**
     * 通过base64验证码 上传超级鹰解析图片1
     * @param base64Image
     * @return
     */
    public static  String getCodeByCodeImage(String base64Image){
        OkHttpRequest okHttpRequest = new OkHttpRequest();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user","qq465561873");
        jsonObject.put("pass","eppmcp57100");
        jsonObject.put("softid","910214");
        jsonObject.put("codetype","1902");
        jsonObject.put("file_base64",base64Image);
        String str = "";
        try {
            str = okHttpRequest.post("http://upload.chaojiying.net/Upload/Processing.php",jsonObject.toJSONString());
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject1 = JSONObject.parseObject(str);
        if(null != jsonObject1.get("err_no") && jsonObject1.get("err_no").toString().equals("0")){
            return (String)jsonObject1.get("pic_str");
        }else{
            return "";
        }

    }
}
