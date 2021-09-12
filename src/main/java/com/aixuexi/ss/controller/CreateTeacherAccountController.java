package com.aixuexi.ss.controller;

/**
 * @author wangyangyang
 * @date 2020/11/11 18:36
 * @description
 **/

import com.aixuexi.ss.common.util.CommonConstant;
import com.aixuexi.ss.entity.ManageCreate;
import com.aixuexi.ss.entity.ManageGetTokensResponse;
import com.aixuexi.ss.entity.UserInfoVo;
import com.aixuexi.ss.enums.TeacherTypeEnum;
import com.aixuexi.ss.service.GhostriderClientService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新建AI好课
 */
@Slf4j
@Controller
@CrossOrigin
@RequestMapping("CreateLiveStreaming")
@Api(tags = "创建老师相关的内容")
public class CreateTeacherAccountController {
    private List<Integer> roleIds = Lists.newArrayList();
    private List<String> roleCodes= Lists.newArrayList();
    protected Map<String, UserInfoVo> userInfoMap = new HashMap<>();

    public void createTeacher(){
        // 获取管理员用户的header，用来在后面做操作
        UserInfoVo wwwManager = this.userInfoMap.get("wwwManager");
        log.info("wwwManager:{}",wwwManager);

        // 添加 角色的 id 和code
        int roleId = TeacherTypeEnum.TEACHER.getRoleId();
        String roleCode = TeacherTypeEnum.TEACHER.getRoleCode();
        this.roleIds.add(roleId);
        this.roleCodes.add(roleCode);

        // 获取 accountToken和financialToken
       // ManageGetTokensResponse tokens = GhostriderClientService.of().getTokens(wwwManager.getHeader());

        // 将密码加密一次
        String aesPassword = null;
        try {
            String password = "ss123456";
            //aesPassword = AESUtil.encrypt(password, CommonConstant.AES.INSTITUTION_PASSWORD_KEY);
            log.info("AES password ,{}",aesPassword);
        } catch (Exception e){
            log.info("加密失败");
        }
        // 获取要添加的手机号，通过数据库执行

        // 构造请求
        ManageCreate manageCreate = ManageCreate.builder()
                .name("自动化测试账号01")
                .telephone("00088980023")
                .portraitPath("")
                .status(1)
               // .accountToken(tokens.getAccountToken())
               // .financialToken(tokens.getFinancialToken())
                .password(aesPassword)
                .roleIds(this.roleIds)
                .roleCodes(this.roleCodes)
                .build();
        // 发出请求
        //BaseResponse<ManageCreateResponse> manageCreateResponse = GhostriderClient.of().manageCreate(wwwManager.getHeader(), manageCreate);
        //log.info("{}",manageCreateResponse);
    }

}
