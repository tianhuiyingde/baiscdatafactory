package com.aixuexi.ss.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wangyangyang
 * @date 2020/11/11 18:37
 * @description 用户角色
 **/
@Data
public class UserInfoVo {
    private String userType;
    private String username;
    private String password;
    private String token;
    private String ptpc;
    private String userId;
    private String loginSystem;
    private List<String> businessUserId;
    private Long gsId;
    /**
     * 如果header中除了ptpc、userId、token之外还有其他内容，则get下来自己再进行put
     */
    private Map<String,String> header;
}
