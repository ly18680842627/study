package com;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Start {
	static {
		try {
			// 初始化log4j
			String log4jPath = Start.class.getClassLoader().getResource("").getPath() + "/log4j.properties";

			System.out.println("初始化Log4j。。。。");

			System.out.println("path is " + log4jPath);

			PropertyConfigurator.configure(log4jPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}
	static Logger logger = Logger.getLogger(Start.class);

	public static void main(String[] args) {
		logger.info("服务开始启动");
		try {
			SpringApplication.run(Start.class, args);
			System.out.println("启动成功!");
		} catch (Exception e) {
			logger.info("服务启动失败:" + e);
			System.out.println("启动失败!");
		}
	}
}
