package com.mierro.main.util;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.mierro.common.common.FileUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * 发送短信工具类
 *
 * @author 黄晓滨
 * @version 1.0
 * @time 2017-4-6 09:56:03
 */
public class SMSUtils {

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    private static final String accessKeyId = "";

    private static final String accessKeySecret = "";

    private static Map<String, Code> CodeCache = new HashedMap<>();

    private static class Code {

        private String code;

        private Date CreateDate = new Date();

        private SendType sendType;

        public Code(String code, SendType sendType) {
            this.code = code;
            this.sendType = sendType;
        }

        public String getCode() {
            return code;
        }

        public Date getCreateDate() {
            return CreateDate;
        }

        public SendType getSendType() {
            return sendType;
        }

        public void setSendType(SendType sendType) {
            this.sendType = sendType;
        }
    }

    public enum SendType {

        LOGIN("登陆", 1),
        UPDATA("修改", 2);

        private String name;
        private Integer id;

        SendType(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(FileUtils.class.getName());

    private static CloseableHttpClient httpClient = null;

    private static String userId;

    private static String account;

    private static String password;

    //编码格式。发送编码格式统一用UTF-8
    private final static String ENCODING = "UTF-8";

    // 初始化
    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        Properties properties = FileUtils.getConfigProperties("config.properties");
        if (properties != null) {
            userId = properties.getProperty("smsUserId");
            account = properties.getProperty("smsAccount");
            password = properties.getProperty("smsPassword");
        }
        LOG.debug("properties: {},smsUserId: {},smsAccount: {},smsPassword: {}", properties, userId,
                account, password);
    }

    public static void removeCodeCache(String key) {
        CodeCache.remove(key);
        System.out.println();
    }

    /**
     * 验证码检测
     *
     * @param phone 手机号码
     * @return null：没有找到该验证码，""：验证码已经过期 ，code：验证码
     */
    public static String checkCode(String phone, SendType sendType) {
        Code code = CodeCache.get(phone);
        if (code == null) {
            return null;
        }
        if (!code.getSendType().equals(sendType)) {
            return null;
        }

        Date date = code.getCreateDate();
        if (date.getTime() >= new Date().getTime() - 1800000) {
            return code.getCode();
        } else {
            removeCodeCache(phone);
            return "";
        }
    }

    /**
     * 发送检测
     *
     * @param telephoneNumber 手机号码
     * @return true：通过，false：距离上次发送未到1分钟
     */
    private static Boolean judge(String telephoneNumber,SendType sendType) {
        Code code = CodeCache.get(telephoneNumber);
        if (code == null) {
            return true;
        } else {
            if(code.getSendType().equals(sendType)){
                Date date = code.getCreateDate();
                return date.getTime() <= new Date().getTime() - 60000;
            }else{
                return true;
            }
        }
    }

//    发送短信
    public static SendSmsResponse SendMessage(String mobile, String code, SendType sendType) throws ClientException {
        Code temp = new Code("123456", sendType);
        CodeCache.put(mobile, temp);
        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        sendSmsResponse.setCode("OK");
        return sendSmsResponse;
    }

    //发送短信
//    public static SendSmsResponse SendMessage(String mobile, String code, SendType sendType)
//            throws ClientException {
//        SendSmsResponse sendSmsResponse = null;
//        if (judge(mobile)) {
//            //可自助调整超时时间
//            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//
//            //初始化acsClient,暂不支持region化
//            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//            IAcsClient acsClient = new DefaultAcsClient(profile);
//
//            //组装请求对象-具体描述见控制台-文档部分内容
//            SendSmsRequest request = new SendSmsRequest();
//            //必填:待发送手机号
//            request.setPhoneNumbers(mobile);
//            //必填:短信签名-可在短信控制台中找到
//            request.setSignName("");
//            //必填:短信模板-可在短信控制台中找到
//            request.setTemplateCode("");
//            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//            request.setTemplateParam("{\"code\":\""+code+"\"}");
//            //hint 此处可能会抛出异常，注意catch
//            try {
//                sendSmsResponse = acsClient.getAcsResponse(request);
//                if (sendSmsResponse.getCode().equals("OK")) {
//                    Code temp = new Code(code, sendType);
//                    CodeCache.put(mobile, temp);
//                } else {
//                    System.out.println(sendSmsResponse.getMessage());
//                    throw new ServiceException(ResponseCode.INTERNAL_SREVER_ERROR, "发送失败");
//                }
//            } catch (Exception e) {
//                throw new ServiceException(ResponseCode.INTERNAL_SREVER_ERROR, "发送失败");
//            }
//        }
//        return sendSmsResponse;
//    }

    /**
     * 基于HttpClient 4.3的通用POST方法
     *
     * @param url       提交的URL
     * @param paramsMap 提交<参数，值>Map
     * @return 提交响应
     */
    public static String post(String url, Map<String, String> paramsMap) {
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpPost method = new HttpPost(url);
            if (paramsMap != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
            }
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null){
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }
}
