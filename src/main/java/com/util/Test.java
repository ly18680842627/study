package com.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {



    public static void main(String[] args) {
// 必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "H:\\phantomjs\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        PhantomJSDriver dr = new PhantomJSDriver(dcaps);
        Cookie cookie1 = new Cookie("verifycode","A9133DE5C1BC395D01369ADD2B88AF49@637426303659371988",".zjy2.icve.com.cn","/",null,false,false);
        Cookie cookie2 = new Cookie("auth","0102171D1C8C9597D808FE172DC85DE997D80801156C0068007600620061007600610072006C006F003500610069006E0062006F003300620077006400710000012F00FF3BA5D8F8CFC4B8D30DAD7F71F700C944281C2B2F",".icve.com.cn","/",new Date("Fri Dec 04 08:13:02 CST 2020"),false,false);
        Cookie cookie3 = new Cookie("Hm_lvt_a3821194625da04fcd588112be734f5b","1607001666,1607004178,1607004692,1607004762",".zjy2.icve.com.cn","/",new Date("Fri Dec 03 22:12:42 CST 2021"),false,false);
        Cookie cookie4 = new Cookie("Hm_lpvt_a3821194625da04fcd588112be734f5b","1607004762",".zjy2.icve.com.cn","/",null,false,false);
        Cookie cookie5 = new Cookie("acw_tc","2f624a7316070047647177715e5da828be9fe8b4eaf0a96a675266fbdf80fa","zjy2.icve.com.cn","/",new Date("Thu Dec 03 22:42:40 CST 2020"),false,false);
        Cookie cookie6 = new Cookie("token","dcjuayasm79pwf7m8ez3ug",".icve.com.cn","/",null,false,false);
        dr.get("https://zjy2.icve.com.cn/student/learning/courseList.html");
        dr.manage().addCookie(cookie1);
        dr.manage().addCookie(cookie1);
        dr.manage().addCookie(cookie2);
        dr.manage().addCookie(cookie3);
        dr.manage().addCookie(cookie4);
        dr.manage().addCookie(cookie5);
        dr.manage().addCookie(cookie6);

        dr.get("https://zjy2.icve.com.cn/student/learning/courseList.html");



        System.out.println(dr.getPageSource());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(new Date("Fri Dec 04 08:13:02 CST 2020"));

        System.out.println(dateString);

    }

}
