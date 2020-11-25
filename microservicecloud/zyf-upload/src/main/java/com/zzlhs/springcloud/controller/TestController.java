package com.zzlhs.springcloud.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
	
	
//	@RequestMapping(value = "/")
//	public String testHello() {
//		return "你好！";
//	}
	
	@RequestMapping(value = "/html", method = RequestMethod.GET)
	public String test() {
		System.out.println("123456789012345678");
		return "test";
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String uploadHtml() {
		return "upload";
	}
	
//	@RequestMapping(value = "/upload", method = RequestMethod.POST)
//	public void upload(@RequestParam("file") MultipartFile file) throws Exception{
//		String filePath = file.getOriginalFilename();
//		String path = "E:\\agree5.0\\组件测试\\组件测试\\";
//		System.out.println("filePath ---> " + filePath + " path ---> " + path);
//		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path + filePath));
//		outputStream.write(file.getBytes());
//		outputStream.flush();
//		outputStream.close();
//	}
	
	 @RequestMapping("/song")
	 public void home(HttpServletRequest request, HttpServletResponse response, @RequestHeader(required = false) String range) {
	        //文件位置
	        File music = new File("E:\\CloudMusic\\test.pdf");
	 
	 
	        //开始下载位置
	        long startByte = 0;
	        //结束下载位置
	        long endByte = music.length() - 1;
	 
	        //有range的话
	        if (range != null && range.contains("bytes=") && range.contains("-")) {
	            //坑爹地方一：http状态码要为206
	            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
	 
	            range = range.substring(range.lastIndexOf("=") + 1).trim();
	            System.out.println("range -> " + range);
	            String[] ranges = range.split("-");
	            try {
	                //判断range的类型
	                if (ranges.length == 1) {
	                    //类型一：bytes=-2343
	                    if (range.startsWith("-")) {
	                        endByte = Long.parseLong(ranges[0]);
	                    }
	                    //类型二：bytes=2343-
	                    else if (range.endsWith("-")) {
	                        startByte = Long.parseLong(ranges[0]);
	                    }
	                }
	                //类型三：bytes=22-2343
	                else if (ranges.length == 2) {
	                    startByte = Long.parseLong(ranges[0]);
	                    endByte = Long.parseLong(ranges[1]);
	                }
	 
	            } catch (NumberFormatException e) {
	                startByte = 0;
	                endByte = music.length() - 1;
	            }
	        } else {
	            //没有ranges即全部一次性传输，需要用200状态码，这一行应该可以省掉，因为默认返回是200状态码
	            response.setStatus(HttpServletResponse.SC_OK);
	        }
	 
	        //要下载的长度（endByte为总长度-1，这时候要加回去）
	        long contentLength = endByte - startByte + 1;
	        //文件名
	        String fileName = music.getName();
	        //获取在响应头中的文件类型
	        String contentType = request.getServletContext().getMimeType(fileName);
	 
	 
	        //各种响应头设置
	        //参考资料：https://www.ibm.com/developerworks/cn/java/joy-down/index.html
	        //坑爹地方二：看代码
	        response.setHeader("Accept-Ranges", "bytes");
	        response.setContentType(contentType);
	        response.setHeader("Content-Type", contentType);
	        //这里文件名换你想要的，inline表示浏览器可以直接使用（比如播放音乐，我方便测试用的）
	        //参考资料：http://hw1287789687.iteye.com/blog/2188500  attachment---> 提示用户保存 inline--->在浏览器中使用
	        response.setHeader("Content-Disposition", "attachment;filename=123.pdf");
	        response.setHeader("Content-Length", String.valueOf(contentLength));
	        //坑爹地方三：Content-Range，格式为
	        // [要下载的开始位置]-[结束位置]/[文件总大小]
	        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + music.length());
	 
	 
	        BufferedOutputStream outputStream = null;
	        RandomAccessFile randomAccessFile = null;
	        //已传送数据大小
	        long transmitted = 0;
	        try {
	            randomAccessFile = new RandomAccessFile(music, "r");
	            outputStream = new BufferedOutputStream(response.getOutputStream());
	            byte[] buff = new byte[4096];
	            int len = 0;
	            System.out.println("用户开始下载 " + startByte + " 一共下载 " + endByte);
	            randomAccessFile.seek(startByte);
	            //坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
	            //不然会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
	            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
	                outputStream.write(buff, 0, len);
	                transmitted += len;
	                //停一下，方便测试（本地下载传输速度特别快，没反应过来就下载好了），实际生产环境中用的时候需要删除这一行
	                Thread.sleep(10);
	            }
	            //处理不足buff.length部分
	            if (transmitted < contentLength) {
	                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
	                outputStream.write(buff, 0, len);
	                transmitted += len;
	            }
	 
	            outputStream.flush();
	            response.flushBuffer();
	            randomAccessFile.close();
	            System.out.println("下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
	 
	        } catch (ClientAbortException e) {
	            System.out.println("用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
	            //捕获此异常表示拥护停止下载
	        } catch (IOException | InterruptedException  e) { // 
	            e.printStackTrace();
	        } finally {
	            try {
	                if (randomAccessFile != null) {
	                    randomAccessFile.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	 
	    }
	
	
//	public ResponseEntity download() {
//		FileSystemResource file = new FileSystemResource("E:\\agree5.0\\组件测试\\组件测试\\1567740086(1).png");
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "attachment; filename=123.png"); 
//		return ResponseEntity.ok(null)
//				.he
//	}
}
