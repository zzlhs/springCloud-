package com.zzlhs.springcloud.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/test")
public class TestController {
	
	
	@RequestMapping(value = "/hello")
	public String testHello() {
		return "你好！";
	}
	
	@RequestMapping(value = "/upload")
	public void upload(@RequestParam("file") MultipartFile file) throws Exception{
		String filePath = file.getOriginalFilename();
		System.out.println("path ---> " + filePath);
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
		outputStream.write(file.getBytes());
		outputStream.flush();
		outputStream.close();
	}
}
