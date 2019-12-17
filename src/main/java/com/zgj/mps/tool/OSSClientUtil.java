package com.zgj.mps.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.zgj.mps.service.SystemSettingService;
import com.zgj.mps.vo.OssItem;
import com.zgj.mps.vo.OssPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 2019/9/18.
 */
@Slf4j
public class OSSClientUtil {
    private static OSSClientUtil ossInstance = null;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String accessUrl;
    private String ossDir;

    public String getAccessUrl() {
        return accessUrl;
    }

    public String getOssDir() {
        return ossDir;
    }

    public OSSClientUtil() {
        log.info("oss初始化=====================>>>>>>>>>>>>>>>>>>");
        SystemSettingService systemSettingService = SpringUtil.getBean(SystemSettingService.class);
        String ossStr = systemSettingService.getSetting("aliyun_oss", "{}");
        if (StrUtil.isNotEmpty(ossStr) && !"{}".equals(ossStr)) {
            JSONObject ossObj = JSONUtil.parseObj(ossStr);
            endpoint = ossObj.get("endpoint") + "";
            accessKeyId = ossObj.get("key") + "";
            accessKeySecret = ossObj.get("secret") + "";
            bucketName = ossObj.get("bucket") + "";
            accessUrl = ossObj.get("url") + "";
            ossDir = ossObj.get("file") + "";
        } else {
            String fileDir = PropertiesUtil.getString("file.dir", "D:\\tmp\\resource");
            ossDir = PropertiesUtil.getString("oss.dir", "mpsoss/");
            log.info("本地上传目录=====================>>>>>>>>>>>>>>>>>>{}", fileDir);
            log.info("本地上传文件=====================>>>>>>>>>>>>>>>>>>{}", ossDir);
            String serverUrl = systemSettingService.getSetting("server_url", "http://localhost:9999/");
            log.info("本地服务器地址{}", serverUrl);
            accessUrl = serverUrl + "/webfile";
        }

    }

    public static synchronized OSSClientUtil getInstance() {
        if (ossInstance == null) {
            ossInstance = new OSSClientUtil();
        }
        return ossInstance;
    }

    public static void clearInstance() {
        ossInstance = null;
    }

    public OSSClient getOssClient() {
        if (endpoint != null && accessKeySecret != null && accessKeyId != null) {
            return new OSSClient(endpoint, accessKeyId, accessKeySecret);
        } else {
            return null;
        }
    }

    public String uploadByByteToOSS(byte[] bcyte, String fileName) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return "";
        }
        String resultStr = null;
        Long fileSize = (long) bcyte.length;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentType(getContentType(fileName));
        String filePath = ossDir + fileName;
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(bcyte),
                metadata);
        StringBuilder sb = new StringBuilder(endpoint + "/" + filePath);
        sb.insert(7, bucketName + ".");
        resultStr = sb.toString();
        ossClient.shutdown();
        return resultStr;
    }

    /**
     * 上传图片至OSS
     *
     * @return String 返回的唯一MD5数字签名
     */
    public String uploadObject2OSS(InputStream is, String fileName) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return "";
        }
        String resultStr = null;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            String filePath = ossDir + fileName;
            metadata.setContentDisposition("filename/filesize=" + (filePath));
            PutObjectResult putResult = ossClient.putObject(bucketName, filePath, is, metadata);
            ossClient.shutdown();
            resultStr = putResult.getETag();
            StringBuilder sb = new StringBuilder(endpoint + "/" + filePath);
            sb.insert(7, bucketName + ".");
            resultStr = sb.toString();
        } catch (Exception e) {
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return "";
        }
        String ret = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            String filePath = ossDir + fileName;
            ossClient.putObject(bucketName, filePath, instream, objectMetadata);
            ret = filePath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            ossClient.shutdown();
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public String uploadFile2OSS(File file, String fileName) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return "";
        }
        String ret = "";
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.length());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            String filePath = ossDir + fileName;
            ossClient.putObject(bucketName, filePath, file, objectMetadata);
            ret = filePath;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ossClient.shutdown();
        }
        return ret;
    }

    /**
     * @param file
     * @return
     * @throws IOException
     * @Title: uploadFileToOSS
     * @Description: 以文件的形式上传文件到OSS
     * @return: String
     */
    public String uploadFileToOSS(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String fileName = random.nextInt(10000) + System.currentTimeMillis() + substring;
        InputStream inputStream = null;
        inputStream = file.getInputStream();
        return uploadFile2OSS(inputStream, fileName);
    }

    public String uploadFileToOSS(MultipartFile file, String fileName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        fileName += substring;
        InputStream inputStream = null;
        inputStream = file.getInputStream();
        return uploadFile2OSS(inputStream, fileName);
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            int idx = fileName.lastIndexOf("/");
            String fileDirName = "";
            String newFileName = "";
            if (idx != -1) {
                fileDirName = fileName.substring(0, idx);
                newFileName = fileName.substring(idx + 1, fileName.length());
                if (File.separator.equals("\\")) {//操作系统是windows，替换文件符号
                    fileDirName = fileDirName.replaceAll("/", "\\\\");
                }
            }
            String localPath = ossDir;
            localPath = localPath.substring(0, localPath.length() - 1);
            File f = FileUtil.touch("D:\\tmp\\resource" + File.separator + localPath + File.separator + fileDirName + File.separator + newFileName + substring);
            FileUtil.writeFromStream(file.getInputStream(), f);
            return localPath + File.separator + fileDirName + File.separator + newFileName + substring;
        } else {
            return uploadFileToOSS(file, fileName);
        }
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") || FilenameExtension.equalsIgnoreCase(".jpg")
                || FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") || FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") || FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        if (FilenameExtension.equalsIgnoreCase(".xls")) {
            return "application/vnd.ms-excel";
        }
        if (FilenameExtension.equalsIgnoreCase(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        return "image/jpeg";
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        System.out.println(fileUrl);
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return getUrl(ossDir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return "";
        }
        // 设置URL过期时间为10年 3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        ossClient.shutdown();
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     *
     * @param fileName 文件名
     * @return 文件的contentType
     */
    private String getContentType(String fileName) {
        log.info("getContentType:" + fileName);
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".pdf".equalsIgnoreCase(fileExtension)) {
            return "application/pdf";
        }
        // 默认返回类型
        return "image/jpeg";
    }

    /**
     * @param fileName
     * @return
     * @Title: getInputStreamByFileUrl
     * @Description: 根据文件路径获取InputStream流
     * @return: InputStream
     */
    public InputStream getInputStreamByFileUrl(String fileName) {
        OSSClient ossClient = getOssClient();
        if (ossClient == null) {
            return null;
        }
        OSSObject ossObject = ossClient.getObject(bucketName, ossDir + fileName);
        ossClient.shutdown();
        return ossObject.getObjectContent();
    }

    /**
     * 根据key删除OSS服务器上的文件 @Title: deleteFile @Description: @param @param
     * ossConfigure @param 配置文件实体 @param filePath 设定文件 @return void 返回类型 @throws
     */
    public void deleteFile(String filePath) {
        OSSClient ossClient = getOssClient();
        if (ossClient != null) {
            InputStream ins = null;
            try {
                ins = new URL(accessUrl + "/" + filePath).openStream();
                ossClient.deleteObject(bucketName, filePath);
            } catch (IOException e) {
                System.out.println("文件不存在");
            } finally {
                ossClient.shutdown();
            }
        }
    }
    //创建Bucket
    public void makeBucket(String bucketName) {
        OSSClient ossClient = getOssClient();
        boolean exist = ossClient.doesBucketExist(bucketName);
        if (exist) {
            log.info("The bucket exist.");
            return;
        }
        ossClient.createBucket(bucketName);
        ossClient.shutdown();
    }

    public OSSObject downloadFile(String key){
        OSSClient client = getOssClient();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        OSSObject object = client.getObject(getObjectRequest);
        return object;
    }

    //创建目录，不能以斜杠“/”开头
    public  void makeDir(String keySuffixWithSlash) {
        OSSClient client = getOssClient();
        /*
         * Create an empty folder without request body, note that the key must
         * be suffixed with a slash
         */
        if (org.apache.commons.lang.StringUtils.isEmpty(keySuffixWithSlash)) {
            return;
        }
        if (!keySuffixWithSlash.endsWith("/")) {
            keySuffixWithSlash += "/";
        }
        client.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
    }

    public OssPage listPage(String dir, String nextMarker,
                            Integer maxKeys, String keyPrefix){
        OSSClient client = getOssClient();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
        if (!org.apache.commons.lang.StringUtils.isEmpty(dir)) {
            listObjectsRequest.setPrefix(dir);
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(keyPrefix)) {
            listObjectsRequest.withPrefix(keyPrefix);
        }
        if (!org.apache.commons.lang.StringUtils.isEmpty(nextMarker)) {
            listObjectsRequest.setMarker(nextMarker);
        }
        if (maxKeys != null) {
            listObjectsRequest.setMaxKeys(maxKeys);
        }
        ObjectListing objectListing = client.listObjects(listObjectsRequest);


        List<OSSObjectSummary> summrayList = objectListing.getObjectSummaries();
        List<OssItem> itemList = summaryToItem(summrayList);
        OssPage page = new OssPage();


        String newxNextMarker = objectListing.getNextMarker();
        page.setNextMarker(newxNextMarker);
        page.setSummaryList(itemList);
        return page;
    }
    //把OSS的对象，转换成自己的。因为OSS的对象没有实现Serialiable，不能序列化。
    private List<OssItem> summaryToItem(
            List<OSSObjectSummary> summaryList) {
        List<OssItem> itemList = new ArrayList<OssItem>();
        for (OSSObjectSummary summary : summaryList) {
            OssItem item = new OssItem();
//                BeanUtils.copyProperties(item, summary);
            item.setLastModified(DateUtil.dateToStrLong(summary.getLastModified()));
            item.setKey(summary.getKey());
            item.setSize(summary.getSize());
            item.setBucketName(summary.getBucketName());
            itemList.add(item);
        }
        return itemList;
    }

    //一次迭代，获得某个目录下的所有文件列表
    public List<OssItem> listAll(String dir) {
        OSSClient client = getOssClient();
        List<OssItem> list = new ArrayList<OssItem>();
        // 查询
        ObjectListing objectListing = null;
        String nextMarker = null;
        final int maxKeys = 1000;
        do {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName).withPrefix(dir).withMarker(nextMarker)
                    .withMaxKeys(maxKeys);
            objectListing = client.listObjects(listObjectsRequest);


            List<OSSObjectSummary> summrayList = objectListing
                    .getObjectSummaries();
            List<OssItem> itemList = summaryToItem(summrayList);
            list.addAll(itemList);
            nextMarker = objectListing.getNextMarker();
        } while (objectListing.isTruncated());
        return list;
    }

    public void deleteKeys(List<String> keys){
        OSSClient client = getOssClient();
        client.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        client.shutdown();
    }
}
