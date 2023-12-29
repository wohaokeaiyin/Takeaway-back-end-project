package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @author SunQi
 * @date 2023/12/5 22:08
 */
@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */

    @PostMapping("upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + substring;

        //创建一个目录对象
        File dir = new File(basePath);
        ///判断当前目录是否存在
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
