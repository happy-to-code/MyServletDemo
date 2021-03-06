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
        // ????????????????????????url
        String errUrl = env.getProperty("webcheck.errUrl");
        log.info("=1====errUrl===== = [{}]", errUrl);
        try {
            String samlPropertiesPath = env.getProperty("file.path");
            log.info("samlPropertiesPath = [{}]", samlPropertiesPath);

            String iamToken = req.getParameter("iamToken");// ????????????Token
            log.info("iamToken::::[{}]", iamToken);

            SPConfiguration conf = SPConfiguration.newInstance(samlPropertiesPath);
            SP sp = SP.newInstance(conf);
            if (StringUtils.isNoneBlank(samlResponseParam)) {
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
                         * ??????????????????????????????
                         * EMPLOYEE -> ????????????
                         * USER -> ????????????
                         * PRIVILEGE -> ??????????????????
                         * e.g.
                         * {"EMPLOYEE":{
                         "organId": "BS0000",
                         "createTime": "2016-05-20 16:55:23",
                         "passwordType": "M_FINGER@??????,M_OTP@??????,M_PASSWORD@??????",
                         "status": "1@??????",
                         "updateTime": "2018-07-10 09:20:59",
                         "telphone": "022-3789124",
                         "userType": "1@????????????",
                         "id": "123456",
                         "certificatesType": "A@?????????",
                         "email": "111@654321.com",
                         "certificatesNumber": "321123198601233443",
                         "address": "test",
                         "name": "??? ???",
                         "uuid": "36dfc549499f477785553c7851a45864",
                         "organName": "BS0000-????????????",
                         "mobile": "13512365478"
                         },"USER":{
                         "ipType": "2@????????????",
                         "organId": "BS0000",
                         "appId": "TPDS",
                         "createdTime": "2016-05-23 13:00:07",
                         "updateTime": "2016-12-01 14:37:27",
                         "sessionToken": "300015yNUcXuhjWa794kUFr85OxLMaPkeFvclhpV0cWsKmpj1bQMNe3ThElHOHkPIqynF7Jmn6gXN7jpZls7AbMyzYD7ikikhYE3dwPSZwu3hGqTV0jQ_u9sLhPnYCsJaJBJcwZ-iDnuqX0S08JV4SbUilvCqDx1jQUn1OeDsvHjFp_MfGMh0dXVdRAeKGmH6fa7JFKe19a-vHESVVKr09zKejkiGIFW6CtsI_-VXPAxLg5Qrt1JAmES_WdCnom7DgIgIfszWGt5VwHUdvi8TlkTkPauMsgoR57WloNw",
                         "name": "??????iamgate",
                         "userId": "123456",
                         "userStatus": "0@??????",
                         "subUserId": "123456",
                         "uuid": "9a80c93fae1942389b9e63d7044cafee",
                         "organName": "BS0000-????????????"
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
                 ???????????????
                 * ????????????????????????????????????????????????????????????????????????????????????
                 * iamToken,sessionToken ?????????????????????????????????????????????????????????????????????????????????
                 */

                // 1??????contentInfoJson????????????????????? ????????????
                ContentInfo contentInfo = JSON.parseObject(contentInfoJson, ContentInfo.class);
                log.info("contentInfo:====>[{}]", contentInfo);
                JSONObject jsonObject = JSON.parseObject(contentInfoJson);
                log.info("jsonObject::[{}]", jsonObject);

                // ?????? subUserId
                User user = contentInfo.getUser();
                if (user == null) {
                    log.info("???????????????????????????");
                    redirect(response, errUrl, "???????????????????????????,??????????????????");
                }
                String subUserId = user.getSubUserId();
                if (subUserId == null) {
                    log.info("subUserId???????????????");
                    redirect(response, errUrl, "subUserId???????????????,??????????????????");
                }

                String sessionToken = user.getSessionToken();
                if (sessionToken == null) {
                    log.info("sessionToken???????????????");
                    redirect(response, errUrl, "sessionToken???????????????,??????????????????");
                }

                String appId = user.getAppId();
                if (appId == null) {
                    log.info("appId???????????????");
                    redirect(response, errUrl, "appId???????????????,??????????????????");
                }

                // String subUserId = "32233332233";
                // ????????????????????????
                String webUrl = env.getProperty("webcheck.url");
                WebCheckParam webCheckParam = new WebCheckParam(subUserId);
                String webCheckParamJsonStr = JSON.toJSONString(webCheckParam);
                P7StringDTO p7StringDTO = new P7StringDTO();
                p7StringDTO.setP7String(webCheckParamJsonStr);
                String p7StringParamJsonStr = JSON.toJSONString(p7StringDTO);
                log.info("p7StringParamJsonStr:===>[{}]", p7StringParamJsonStr);

                String jsonResponse = "";
                jsonResponse = OKHttpUtil.postJsonParams(webUrl, p7StringParamJsonStr);
                log.info("jsonResponse:[{}]", jsonResponse);
                if (StringUtils.isBlank(jsonResponse)) {
                    log.info("????????????????????????");
                    redirect(response, errUrl, "????????????????????????,??????????????????");
                }
                //  ???????????? jsonResponse ???????????????????????????
                WebCheckResponse webCheckResponse = JSON.parseObject(jsonResponse, WebCheckResponse.class);
                log.info("webCheckResponse:[{}]", webCheckResponse);
                if (webCheckResponse == null) {
                    log.info("??????????????????????????????");
                    redirect(response, errUrl, "??????????????????????????????,??????????????????");
                    return;
                }
                Integer code = webCheckResponse.getCode();
                if (code != 200) {
                    log.info("???????????????????????????");
                    redirect(response, errUrl, "???????????????????????????,??????????????????");
                }
                String jwt = webCheckResponse.getData();
                log.info("jwt====>[{}]", jwt);

                // ????????????
                // String res = "success";
                log.info("callback param---->iamToken:[{}],sessionToken:[{}],appId:[{}]", iamToken, sessionToken, appId);
                String res = webServiceCallBack(iamToken, sessionToken, appId);
                log.info("callback response===>[{}]", res);
                if (res == "success") {
                    log.info("redirect begin.....");
                    // ?????????
                    response.setStatus(response.SC_MOVED_TEMPORARILY);
                    response.setHeader("Location", jwt);
                } else {
                    log.info("callback fail???[{}]", res);
                    redirect(response, errUrl, res + ",??????????????????");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, errUrl, e.toString() + ",??????????????????");
        }
    }

    // webservice ????????????
    private String webServiceCallBack(String iamToken, String sessionToken, String appId) {
        // ??????webservice????????????  ???????????????????????????appid???seesiontoken???iamtoken
        String wsdlUrl = env.getProperty("wsdl.url");
        log.info("wsdlUrl===>[{}]", wsdlUrl);
        // ?????????????????????
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdlUrl);
        Object[] objects;
        try {
            //?????????????????????????????????
            objects = client.invoke("verifySessionAgain", sessionToken, appId, iamToken);

            // ???????????????????????????
            IamgateWsResponse iamgateWsResponse = JSON.parseObject(JSONObject.toJSONString(objects[0]), IamgateWsResponse.class);
            log.info("iamgateWsResponse:[{}]", iamgateWsResponse);
            // Status 1@?????? 3@??????
            // code 1:?????? 0:?????? -1:??????????????????
            if (iamgateWsResponse.getStatus().equals("success") && iamgateWsResponse.getCode() == 0) {
                log.info("callback success");
                return "success";
            }

            // ???????????????
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
