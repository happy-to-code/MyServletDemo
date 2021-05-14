package com.tongji.samlsp.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.isprint.am.saml.AuthnResponse;
import com.isprint.am.saml.sp.SP;
import com.isprint.am.saml.sp.SPConfiguration;
import com.tongji.samlsp.model.WebCheckParam;
import com.tongji.samlsp.model.WebCheckResponse;
import com.tongji.samlsp.model.pojo.ContentInfo;
import com.tongji.samlsp.model.pojo.IamgateWsResponse;
import com.tongji.samlsp.model.pojo.P7StringDTO;
import com.tongji.samlsp.model.pojo.User;
import com.tongji.samlsp.util.OKHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Service
@WebServlet(urlPatterns = "/v1/api/acs")
public class SAMLSPServlet extends HttpServlet {
    @Autowired
    private Environment env;


    public void doPost(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String samlResponseParam = req.getParameter("SAMLResponse");
        String samlRelayState = req.getParameter("RelayState");
        acs(req, response, samlResponseParam, samlRelayState);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // String site = "https://www.baidu.com";
        // redirect(resp, site,"");
        doPost(req, resp);
    }

    private void redirect(HttpServletResponse resp, String url, String param) {
        if (StringUtils.isNoneBlank(param)) {
            try {
                String newParam = URLEncoder.encode(param, "utf-8");
                url = url + "?error=" + newParam;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        log.info("=2====>redirect Url:[{}]", url);
        resp.setStatus(resp.SC_MOVED_TEMPORARILY);
        resp.setHeader("Location", url);
        return;
    }

    public void acs(HttpServletRequest req, HttpServletResponse response,
                    String samlResponseParam, String samlRelayState) {
        log.info("samlResponseParam:[{}]", samlResponseParam);
        log.info("samlRelayState:[{}]", samlRelayState);
        // 发生错误时跳转的url
        String errUrl = env.getProperty("webcheck.errUrl");
        log.info("=1====errUrl===== = [{}]", errUrl);
        try {
            String samlPropertiesPath = env.getProperty("file.path");
            log.info("samlPropertiesPath = [{}]", samlPropertiesPath);

            String iamToken = req.getParameter("iamToken");// 统一认证Token
            log.info("iamToken::::[{}]", iamToken);

            SPConfiguration conf = SPConfiguration.newInstance(samlPropertiesPath);
            SP sp = SP.newInstance(conf);
            if (samlResponseParam != null && samlResponseParam != "") {
                AuthnResponse samlResponse = sp.parseAuthnResponse(samlResponseParam);
                log.info("samlResponse:[{}]", samlResponse);
                log.info("samlResponse.getAttributes():[{}]", samlResponse.getAttributes());
                StringBuilder userAttributes = new StringBuilder();
                System.out.println(samlResponse.getAttributes());
                String contentInfoJson = "";
                for (String name : samlResponse.getAttributes().keySet()) {
                    if (userAttributes.length() > 0) {
                        userAttributes.append(",");
                    }
                    if (name.equals("content")) {
                        /**
                         * 返回字段属性主体事例
                         * EMPLOYEE -> 员工信息
                         * USER -> 用户信息
                         * PRIVILEGE -> 用户权限信息
                         * e.g.
                         * {"EMPLOYEE":{
                         "organId": "BS0000",
                         "createTime": "2016-05-20 16:55:23",
                         "passwordType": "M_FINGER@指纹,M_OTP@短信,M_PASSWORD@密码",
                         "status": "1@正常",
                         "updateTime": "2018-07-10 09:20:59",
                         "telphone": "022-3789124",
                         "userType": "1@本行员工",
                         "id": "123456",
                         "certificatesType": "A@身份证",
                         "email": "111@654321.com",
                         "certificatesNumber": "321123198601233443",
                         "address": "test",
                         "name": "马 亮",
                         "uuid": "36dfc549499f477785553c7851a45864",
                         "organName": "BS0000-上海银行",
                         "mobile": "13512365478"
                         },"USER":{
                         "ipType": "2@生产系统",
                         "organId": "BS0000",
                         "appId": "TPDS",
                         "createdTime": "2016-05-23 13:00:07",
                         "updateTime": "2016-12-01 14:37:27",
                         "sessionToken": "300015yNUcXuhjWa794kUFr85OxLMaPkeFvclhpV0cWsKmpj1bQMNe3ThElHOHkPIqynF7Jmn6gXN7jpZls7AbMyzYD7ikikhYE3dwPSZwu3hGqTV0jQ_u9sLhPnYCsJaJBJcwZ-iDnuqX0S08JV4SbUilvCqDx1jQUn1OeDsvHjFp_MfGMh0dXVdRAeKGmH6fa7JFKe19a-vHESVVKr09zKejkiGIFW6CtsI_-VXPAxLg5Qrt1JAmES_WdCnom7DgIgIfszWGt5VwHUdvi8TlkTkPauMsgoR57WloNw",
                         "name": "马亮iamgate",
                         "userId": "123456",
                         "userStatus": "0@正常",
                         "subUserId": "123456",
                         "uuid": "9a80c93fae1942389b9e63d7044cafee",
                         "organName": "BS0000-上海银行"
                         },"PRIVILEGE":[]}
                         */
                        contentInfoJson = samlResponse.getAttributes().get("content");
                        log.info("contentInfoJson:[{}]", contentInfoJson);
                    }
                    userAttributes.append(name).append("=").append(samlResponse.getAttributes().get(name));
                }

                log.info("samlResponse.getUserIdentity():[{}]", samlResponse.getUserIdentity());
                log.info("userAttributes.toString():[{}]", userAttributes.toString());
                log.info("samlResponse.getAuthnContext():[{}]", samlResponse.getAuthnContext());

                /**
                 注意事项：
                 * 用户号验证在接入系统中是否存在，如果不存在，请拒绝登录。
                 * iamToken,sessionToken 需要调用统一认证验证接口进行验证。验证失败，拒绝登录。
                 */

                // 1、将contentInfoJson反序列化，取出 用户信息
                ContentInfo contentInfo = JSON.parseObject(contentInfoJson, ContentInfo.class);
                log.info("contentInfo:====>[{}]", contentInfo);
                JSONObject jsonObject = JSON.parseObject(contentInfoJson);
                log.info("jsonObject::[{}]", jsonObject);

                // 获取 subUserId
                User user = contentInfo.getUser();
                if (user == null) {
                    log.info("用户信息不可以为空");
                    redirect(response, errUrl, "用户信息不可以为空");
                }
                String subUserId = user.getSubUserId();
                if (subUserId == null) {
                    log.info("subUserId不可以为空");
                    redirect(response, errUrl, "subUserId不可以为空");
                }

                String sessionToken = user.getSessionToken();
                if (sessionToken == null) {
                    log.info("sessionToken不可以为空");
                    redirect(response, errUrl, "sessionToken不可以为空");
                }

                String appId = user.getAppId();
                if (appId == null) {
                    log.info("appId不可以为空");
                    redirect(response, errUrl, "appId不可以为空");
                }

                // String subUserId = "32233332233";
                // 调用管理系统接口
                String webUrl = env.getProperty("webcheck.url");
                WebCheckParam webCheckParam = new WebCheckParam(subUserId);
                String webCheckParamJsonStr = JSON.toJSONString(webCheckParam);
                P7StringDTO p7StringDTO = new P7StringDTO();
                p7StringDTO.setP7String(webCheckParamJsonStr);
                String p7StringParamJsonStr = JSON.toJSONString(p7StringDTO);
                log.info("p7StringParamJsonStr:===>[{}]", p7StringParamJsonStr);

                String jsonResponse = OKHttpUtil.postJsonParams(webUrl, p7StringParamJsonStr);
                log.info("jsonResponse:[{}]", jsonResponse);
                if (jsonResponse == "" || jsonResponse.length() <= 0) {
                    log.info("调用管理系统出错");
                    redirect(response, errUrl, "调用管理系统出错");
                }
                //  反序列化 jsonResponse 判断有没有验证成功
                WebCheckResponse webCheckResponse = JSON.parseObject(jsonResponse, WebCheckResponse.class);
                log.info("webCheckResponse:[{}]", webCheckResponse);
                if (webCheckResponse == null) {
                    log.info("解析管理系统参数出错");
                    redirect(response, errUrl, "解析管理系统参数出错");
                }
                Integer code = webCheckResponse.getCode();
                if (code != 200) {
                    log.info("管理系统验证未通过");
                    redirect(response, errUrl, "管理系统验证未通过");
                }
                String jwt = webCheckResponse.getData();
                log.info("jwt====>[{}]", jwt);

                // 回调验证
                // String res = "success";
                log.info("callback param---->iamToken:[{}],sessionToken:[{}],appId:[{}]", iamToken, sessionToken, appId);
                String res = webServiceCallBack(iamToken, sessionToken, appId);
                log.info("callback response===>[{}]", res);
                if (res == "success") {
                    log.info("redirect begin.....");
                    // 重定向
                    response.setStatus(response.SC_MOVED_TEMPORARILY);
                    response.setHeader("Location", jwt);
                } else {
                    log.info("callback fail：[{}]", res);
                    redirect(response, errUrl, res);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, errUrl, e.toString());
        }
    }

    // webservice 回调方法
    private String webServiceCallBack(String iamToken, String sessionToken, String appId) {
        // 调用webservice接口验证  回调认证接口入参是appid，seesiontoken，iamtoken
        String wsdlUrl = env.getProperty("wsdl.url");
        log.info("wsdlUrl===>[{}]", wsdlUrl);
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdlUrl);
        Object[] objects;
        try {
            //按照各自的文档传入参数
            objects = client.invoke("verifySessionAgain", sessionToken, appId, iamToken);

            // 将字符串转换成对象
            IamgateWsResponse iamgateWsResponse = JSON.parseObject(JSONObject.toJSONString(objects[0]), IamgateWsResponse.class);
            log.info("iamgateWsResponse:[{}]", iamgateWsResponse);
            // Status 1@正常 3@停用
            // code 1:失败 0:成功 -1:密码需要重置
            if (iamgateWsResponse.getStatus().equals("success") && iamgateWsResponse.getCode() == 0) {
                log.info("callback success");
                return "success";
            }

            // 错误返回值
            StringBuffer sb = new StringBuffer("status:");
            sb.append(iamgateWsResponse.getStatus())
                    .append(",code:")
                    .append(iamgateWsResponse.getCode())
                    .append(",message:")
                    .append(iamgateWsResponse.getMessage());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
