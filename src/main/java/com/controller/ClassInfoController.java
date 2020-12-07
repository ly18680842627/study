package com.controller;

import com.util.CodeApi;
import org.apache.axis.encoding.Base64;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * 获取学生所有未完成课程信息
 */
@RequestMapping("/classinfo")
@Controller
public class ClassInfoController {

    /**
     * 根据数据库中学生帐号密码查询对应学生需要学习的课程
     */
    @RequestMapping("/getclassinfo")
    @ResponseBody
    public String getClassFoStudent(){
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

        dr.get("https://zjy2.icve.com.cn/");

        //获取登录名元素
        WebElement username = dr.findElement(By.name("userName"));
        username.clear();
        //为元素赋值登录名
        username.sendKeys("2019007624");

        //获取密码元素
        WebElement passwordEl = dr.findElement(By.name("userPassword"));
        //为元素赋值登录密码
        passwordEl.sendKeys("lyy334420yy@");

        //获取验证码图片
        WebElement webElement = dr.findElement(By.xpath("//*[@id='x-modify']/div/img[2]"));
        String bse64Image = "";
        try {
            bse64Image = captureElement(webElement);
        } catch (IOException e) {
            dr.close();
            e.printStackTrace();
        }
        if(StringUtils.isEmpty(bse64Image)){
            dr.close();
            return "未获取到验证码图片无法进行操作";
        }
        String code = CodeApi.getCodeByCodeImage(bse64Image);
        if(StringUtils.isEmpty(code)){
            dr.close();
            return "获取到验证码有误无法完成操作";
        }
        //获取验证码元素
        WebElement yzmEl = dr.findElement(By.name("photoCode"));
        //为元素赋值验证码
        yzmEl.sendKeys(code);
        //yzmEl.sendKeys("1234");
        //获取登录按钮
        WebElement login = dr.findElement(By.id("btnLogin"));

        Actions action = new Actions(dr);//-------------------------------------------声明一个动作
        action.moveToElement(username).build().perform();//------------------------------------执行滑动动作
        login.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String errString = "";
        try {
            errString = dr.findElement(By.className("txtBox")).getText();
        }catch (Exception e){
        }
        if(!StringUtils.isEmpty(errString)){
            return errString;
        }

        Set<Cookie> cookies = dr.manage().getCookies();


        return "11111";
    }

    //根据元素截取图片
    public static String captureElement(WebElement element) throws RasterFormatException, IOException {
        // TODO Auto-generated method stub
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        // 截图整个页面
        File screen = ((RemoteWebDriver) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        java.awt.Rectangle rect = new java.awt.Rectangle(width, height);
        // 得到元素的坐标
        Point p = element.getLocation();
        String base64 = "";
        try {
            BufferedImage img = ImageIO.read(screen);
            // 获得元素的高度和宽度
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
            //输出流
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(dest, "png", stream);
            base64 = Base64.encode(stream.toByteArray());
            // 存为png格式
            ImageIO.write(dest, "png", screen);
        }catch (RasterFormatException e){
            throw new RasterFormatException(e.getMessage());
        }
        catch (IOException e) {
            throw  new IOException(e.getMessage());
        }
        return base64;
    }




}
