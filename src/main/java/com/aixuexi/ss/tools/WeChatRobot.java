package com.aixuexi.ss.tools;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.HttpMethod.POST;
@Slf4j
public class WeChatRobot
{
    private RestTemplate restTemplate = new RestTemplate();
    String WebHookUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=a247a6e0-f94c-4bbc-8d82-21f6ebcc288f";

    public void senMsgDemo(String content) throws URISyntaxException {
        JSONObject reqObj = new JSONObject();
        reqObj.put("msgtype","text");
        JSONObject textObj =  new JSONObject();
        textObj.put("content",content);
        reqObj.put("text",textObj);
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        URI url = new URI(WebHookUrl);
        log.info("获取AXGToken请求信息："+reqObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(reqObj, headers, POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        if(responseEntity.getStatusCodeValue() !=200)
        {
            log.error(responseEntity.getBody().toString());
        }

    }

    public static void main(String[] args) {
        WeChatRobot weChatRobot = new WeChatRobot();
        try{
            weChatRobot.senMsgDemo("测试信息");
        }catch (Exception e)
        {
            e.getMessage();
        }

    }
}
