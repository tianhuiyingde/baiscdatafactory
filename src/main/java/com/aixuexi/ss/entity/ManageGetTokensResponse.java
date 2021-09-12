package com.aixuexi.ss.entity;

import lombok.Data;

/**
 * @author wangyangyang
 * @date 2020/11/11 18:58
 * @description
 **/
@Data
public class ManageGetTokensResponse {
    /**
     * financialToken : 429c7c1186648b5084cd9f65004ac08a
     * accountToken : 8d8c137010a64202b385b0bbd185b2e4
     */

    private String financialToken;
    private String accountToken;
}
