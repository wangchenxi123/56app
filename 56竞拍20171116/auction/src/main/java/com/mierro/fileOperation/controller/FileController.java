package com.mierro.fileOperation.controller;

import com.mierro.common.common.FileUtils;
import com.mierro.common.common.ResponseCode;
import com.mierro.common.common.ResultMessage;
import com.mierro.common.common.ServiceException;
import com.mierro.fileOperation.common.UploadType;
import com.mierro.fileOperation.po.UploadFile;
import com.mierro.fileOperation.service.IUploadService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 文件管理控制器
 * 
 * @version 1.0
 * @author 唐亮
 * @time 2016-5-13 19:49:04
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Resource
    private IUploadService uploadService;

    /**
     * 上传单个文件，
     * @param file 上传文件
     * @param uploadType 上传文件类型，默认为OTHERS
     * @return Result
     * @throws IOException
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST)
    public ResultMessage uploadImg(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "type", required = false) UploadType uploadType)throws IOException {
        //判断是否是图片
        if (!uploadType.equals(UploadType.IMAGE)){
            return new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"文件类型与要求的不相符");
        }
        InputStream inputStream ;
        UploadFile uploadVo = new UploadFile();
        String temp = file.getOriginalFilename();
        String [] getType = temp.split("\\.");
        String type = file.getContentType().split("/")[1];
        if (getType.length < 2){
            temp = getType[0]+"."+type;
        }

        if(temp.contains(".png")){
            temp = getType[0]+".gif";
            type = "gif";
        }
        String fileUrl;
        String fileName;

        fileUrl = uploadType.getRelativeURL(temp);
        fileName = FileUtils.getResourcePath(fileUrl);
        if(fileName == null){
            return new ResultMessage(ResponseCode.ACCESSDENIED,"生成保存目录失败");
        }
        inputStream = file.getInputStream();
        File file1 = new File(fileName);
        //判断目标文件所在的目录是否存在
        if (!file1.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if (!file1.getParentFile().mkdirs()) {
                return new ResultMessage(ResponseCode.ACCESSDENIED,"创建目标文件所在目录失败！");
            }
        }

        //对图片进行等比压缩
        try{
            Thumbnails.of(inputStream)
                    .scale(1f)//等比压缩比例
                    .outputQuality(0.7f)//输出的质量
                    .outputFormat(type)//输出的文件类型
                    .toFile(fileName);
        }catch (Exception e){
            fileUrl =uploadType.getRelativeURL(file.getOriginalFilename());
            FileUtils.create(file.getInputStream(), FileUtils.getResourcePath(fileUrl));
        }
        uploadVo.setContentType(file.getContentType());
        uploadVo.setType(uploadType);
        uploadVo.setUrl(fileUrl);
        uploadVo.setCreateTime(new Date());
        uploadVo.setFileName(fileName);
        uploadVo = uploadService.save(uploadVo);
        return new ResultMessage(ResponseCode.OK,"上传成功").putParam("file", uploadVo);
    }

    /**
     * 上传单个文件，
     * 
     * @param file 上传文件
     * @param uploadType 上传文件类型，默认为OTHERS
     * @return Result
     * @throws IOException 
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResultMessage upload(@RequestParam("file") MultipartFile file,
                                @RequestParam(value = "uploadType", required = false) UploadType uploadType)throws IOException {

        if (uploadType == null || !uploadType.check(file.getContentType())) {
            return new ResultMessage(ResponseCode.INTERNAL_SREVER_ERROR,"文件类型与要求的不相符");
        }
        UploadFile uploadVo = new UploadFile();
        uploadVo.setContentType(file.getContentType());
        uploadVo.setType(uploadType);
        uploadVo.setCreateTime(new Date());
        uploadVo.setUrl(uploadType.getRelativeURL(file.getOriginalFilename()));
        uploadVo.setFileName(file.getOriginalFilename());
        uploadVo = uploadService.save(uploadVo);
        // 复制文件到服务器上
        FileUtils.create(file.getInputStream(), FileUtils.getResourcePath(uploadVo.getUrl()));
        return new ResultMessage(ResponseCode.OK,"上传成功").putParam("file", uploadVo);
    }

    /**
     * 下载文件
     * 
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("fid") Long fid, HttpServletResponse response)
        throws IOException {
        UploadFile fileVo = uploadService.findOne(fid);
        File file = FileUtils.getResourceFile(fileVo.getUrl());
        if (file == null) {
            throw new ServiceException("文件丢失!");
        }
        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
            "attachment; filename=" + URLEncoder.encode(fileVo.getFileName(), "utf-8"));// 设定输出文件头
        response.setContentType(fileVo.getContentType());// 定义输出类型
        FileUtils.inToout(new FileInputStream(file), response.getOutputStream());
    }

    /**
     * 查看文件
     *
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/see", method = RequestMethod.GET)
    public void see(@RequestParam("fid") Long fid, HttpServletResponse response,HttpServletRequest request)
        throws IOException {
        UploadFile fileVo = uploadService.findOne(fid);
        Long date = request.getDateHeader("If-Modified-Since");
        if (new Date().getTime()-date < 1000*60*29){
            response.setDateHeader("Expires", date+1000*60*30);//缓存30分钟
            response.setHeader("ETag", request.getHeader("If-None-Match"));
            response.setDateHeader("Last-Modified", date);
            response.setHeader("Cache-Control", "public");
            response.setHeader("Pragma", "Pragma");
            response.setContentType(fileVo.getContentType());// 定义输出类型
            response.setHeader("Content-disposition",
                    "inline; filename=" + URLEncoder.encode(fileVo.getFileName(), "utf-8"));// 设定输出文件头
            response.setStatus(304);
        }else{
            if (fileVo == null){
                throw new ServiceException("找不到文件记录!");
            }
            File file = FileUtils.getResourceFile(fileVo.getUrl());
            if (file == null) {
                throw new ServiceException("文件丢失!");
            }
            response.reset();// 不加这一句的话会出现下载错误
            response.setHeader("Content-disposition",
                    "inline; filename=" + URLEncoder.encode(fileVo.getFileName(), "utf-8"));// 设定输出文件头
            response.setContentType(fileVo.getContentType());// 定义输出类型
            response.setDateHeader("Expires", new Date().getTime()+1000*60*30);
            response.setHeader("ETag", Long.toString(new Date().getTime()));
            response.setDateHeader("Last-Modified", new Date().getTime());
            response.setHeader("Cache-Control", "public");
            response.setHeader("Pragma", "Pragma");
            response.setContentType(fileVo.getContentType());// 定义输出类型
            FileUtils.inToout(new FileInputStream(file), response.getOutputStream());
        }
    }

}
