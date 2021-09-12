package com.aixuexi.ss.service.createclass;

import com.aixuexi.ss.common.dns.LoadDns;
import com.aixuexi.ss.entity.LecturerTeacherTokenInfo;
import com.aixuexi.ss.common.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
@Configuration
public class CrateStreamingClassService
{
    @Value("${liveclass.axgGetTokenUrl}")
    private String axgGetTokenUrl;
    @Value("${liveclass.createLiveUrl}")
    private String createLiveUrl;
    @Value("${liveclass.initScheduleUrl}")
    private String initScheduleUrl;
    @Value("${liveclass.updateClassReleaseUrl}")
    private String updateClassReleaseUrl;
    @Value("${liveclass.applyPassUrl}")
    private String applyPassUrl;
    @Value("${common.queryDetailsUrl}")
    private String queryDetailsUrl;
    @Value("${aiclass.createAiClassUrl}")
    private String createAiClassUrl;
    @Value("${aiclass.listByPeriodUrl}")
    private String listByPeriodUrl;
    @Value("${aiclass.listForCreateLiveClassUrl}")
    private String listForCreateLiveClassUrl;
    @Value("${aiclass.classTypeListUrl}")
    private String classTypeListUrl;
    @Value("${aiclass.marketSettingUrl}")
    private String marketSettingUrl;
    @Value("${aiclass.completionUrl}")
    private String completionUrl;
    @Value("${aiclass.dtMarketSettingUrl}")
    private String dtMarketSettingUrl;
    @Value("${aiclass.aiCourseListUrl}")
    private String  aiCourseListUrl;
    @Value("${aiclass.teacherListUrl}")
    private String teacherListUrl;
    @Value("${aiclass.assistantTeacherListUrl}")
    private String assistantTeacherListUrl;

    RestTemplate restTemplate = new RestTemplate();
    TimeUtil timeUtil = new TimeUtil();
    /**
     * 获取爱笑管登录token
     * @param teacherInfo
     * @return
     */
    public LecturerTeacherTokenInfo getAXGTokenInfo(JSONObject teacherInfo) throws URISyntaxException {
        LecturerTeacherTokenInfo axgTokenInfo = new LecturerTeacherTokenInfo();
        Integer loginType = 1;
        String loginSystem = "pc";
        JSONObject reqObj = new JSONObject();
        reqObj.put("username",teacherInfo.getString("userName"));
        reqObj.put("password",teacherInfo.getString("passWord"));
        reqObj.put("loginType",loginType);
        reqObj.put("loginSystem",loginSystem);
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        URI url = new URI(axgGetTokenUrl);
        log.info("获取AXGToken请求信息："+reqObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(reqObj, headers, POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("获取AXGToken返回信息："+repObj.toJSONString());
        JSONObject repBody = repObj.getJSONObject("body");
        axgTokenInfo.setPtpc(repBody.getString("axxProof"));
        axgTokenInfo.setUserId(repBody.getString("userId"));
        return  axgTokenInfo;
    }

    /**
     * http请求headers
     * @param
     * @param axgTokenInfo
     * @returnapplyClass
     */
    public MultiValueMap aXGtHttpHeaders(JSONObject axgTokenInfo)  {
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        headers.add("ptpc",axgTokenInfo.getString("ptpc"));
        headers.add("userId",axgTokenInfo.getString("userId"));
        headers.add("token","axx");
        return headers;
    }
    /**
     * http请求headers旧版
     * @param
     * @param axgTokenInfo
     * @returnapplyClass
     */
    public MultiValueMap aXGtHttpHeaders(LecturerTeacherTokenInfo axgTokenInfo) throws URISyntaxException {
        MultiValueMap headers  = new LinkedMultiValueMap<>();
        headers.add("Content-Type","application/json");
        headers.add("ptpc",axgTokenInfo.getPtpc());
        headers.add("userId",axgTokenInfo.getUserId());
        headers.add("token","axx");
        log.info("头部信息："+"ptpc:"+axgTokenInfo.getPtpc()+" " +"userId:"+axgTokenInfo.getUserId());
        return headers;
    }




    /**
     * 爱笑管-根据学期展现学科
     * @param axgTokenInfo
     * @return
     */
    public JSONObject listSubjectByPeriod(int period,JSONObject axgTokenInfo) throws URISyntaxException {
        String valueUrl = listByPeriodUrl
                + "?period=" + period;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("爱笑管-根据学期展现学科的Url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("爱笑管-根据学期展现学科的返回信息是：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 爱笑管-根据学科展现年级
     * @param axgTokenInfo
     * @return
     */
    public JSONObject listForCreateLiveClass(int period,int subjectProductId,JSONObject axgTokenInfo) throws URISyntaxException {
        String valueUrl = listForCreateLiveClassUrl
                + "?period=" + period
                + "&subjectProductId=" + subjectProductId;

        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("根据学科展现年级的url信息是：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);

        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("根据学科展现年级的返回信息是：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 爱笑管-根据年级课程类型展现班型
     * @param axgTokenInfo
     * @return
     */
    public JSONObject classTypeList(JSONObject reqBody,JSONObject axgTokenInfo) throws URISyntaxException {
        JSONObject classTypeListObj = new JSONObject();
        classTypeListObj.put("classStyle",reqBody.getString("classStyle"));
        classTypeListObj.put("classTypeType",reqBody.getString("classTypeType"));
        classTypeListObj.put("gradeId",reqBody.getString("gradeId"));
        classTypeListObj.put("isOpenClass",reqBody.getString("isOpenClass"));
        classTypeListObj.put("period",reqBody.getString("period"));
        classTypeListObj.put("subjectProductId",reqBody.getString("subjectProductId"));

        URI url = new URI(classTypeListUrl);
        log.info("爱笑管-根据年级课程类型展现班型请求信息："+classTypeListObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(classTypeListObj, aXGtHttpHeaders(axgTokenInfo), POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("爱笑管-根据年级课程类型展现班型返回信息："+repObj.toJSONString());

        return repObj;
    }

    /**
     * 爱笑管-获取AI课型信息
     * @param axgTokenInfo
     * @return
     */
    public JSONObject getAICourseLsit(Integer classTypeId,JSONObject axgTokenInfo) throws URISyntaxException {
        String valueUrl = aiCourseListUrl
                + "?classTypeId=" + classTypeId;
        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        log.info("获取课型Url：" + valueUrlDelThree);
        URI initUrl = new URI(valueUrlDelThree);
        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("根据学科展现年级的返回信息是：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 获取爱校管主讲信息列表
     * @param reqBody
     * @param axgTokenInfo
     * @return
     */
    public JSONObject getTeacherList(JSONObject reqBody, JSONObject axgTokenInfo)throws URISyntaxException
    {

        log.info("获取主讲信息列表url：" + teacherListUrl);
        URI initUrl = new URI(teacherListUrl);
        RequestEntity requestEntity = new RequestEntity<>(reqBody, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("主讲信息列表：" + repObj.toJSONString());
        return repObj;
    }

    /**
     * 获取爱校管主讲信息列表
     * @param reqBody
     * @param axgTokenInfo
     * @return
     */
    public JSONArray getAssistantTeacherList(JSONObject reqBody, JSONObject axgTokenInfo)throws URISyntaxException
    {
        Integer subjectProductId = reqBody.getInteger("subjectProductId");
        Integer gradeId = reqBody.getInteger("gradeId");
        String valueUrl = assistantTeacherListUrl
                + "&subjectProductId=" + subjectProductId
                + "&gradeId="+ gradeId;
        log.info("获取助教信息列表url：" + valueUrl);
        String valueUrlDelThree = valueUrl.replaceAll("\\s*", "");
        URI initUrl = new URI(valueUrlDelThree);
        RequestEntity requestEntity = new RequestEntity<>(reqBody, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity, JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        JSONObject teacherObj = repObj.getJSONObject("body").getJSONObject("teachers");
        Iterator iter =  teacherObj.keySet().iterator();
        JSONArray newTeacher = new JSONArray();
        while (iter.hasNext())
        {
            newTeacher.addAll(teacherObj.getJSONArray(iter.next().toString()));
        }
        log.info("助教信息列表：" + newTeacher.toJSONString());
        return newTeacher;
    }



    /**
     * 创建直播课&排课
     * 排课默认日期为：默认时间为当前日期开始，每周一，20:00-22:00
     * @param axgTokenInfo
     * @return
     */
    public JSONObject createLive(JSONObject courseInfo, JSONObject axgTokenInfo) throws URISyntaxException {
        URI url = new URI(createLiveUrl);
        log.info("创建直播课请求信息："+courseInfo.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(courseInfo, aXGtHttpHeaders(axgTokenInfo), POST, url);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("创建直播课返回信息："+repObj.toJSONString());

        return repObj;
    }



    /**
     * 给时间加上几个小时
     * @param date 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static String addDateMinut(Date date, int hour){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
        System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return format.format(date);
    }
    /**
     * 排课默认时间为所选时间加一个小时
     * @param classId
     * @param classTypeId
     * @return
     */
    public JSONObject initSchedule(Integer classId, Integer classTypeId, JSONObject axgTokenInfo, Date dateTimeInfo) throws URISyntaxException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(dateTimeInfo);
        Date date =  sdf.parse(s);
        SimpleDateFormat sdf12 = new SimpleDateFormat("HH:mm:ss");
        String s123 = sdf12.format(dateTimeInfo);
        log.info("建课时间："+s123.substring(0,2)+"   "+s123.substring(3,5)
                +"   "+Integer.valueOf(s123.substring(0,2))+2
                +"   "+Integer.valueOf(s123.substring(3,5))+5);

        String newdateTime = addDateMinut(dateTimeInfo, 1);
        log.info("建课时间："+newdateTime+"   "+newdateTime.substring(11,13)
                +"   "+newdateTime.substring(14,16));

        JSONObject initScheduleObj = new JSONObject();
        initScheduleObj.put("classId",classId);
        initScheduleObj.put("beginDate", date);
        initScheduleObj.put("skipHolidays",true);
        JSONArray teachingTimesArr = new JSONArray();
        JSONObject teachingTimesObj = new JSONObject();
        teachingTimesObj.put("beginHour",s123.substring(0,2));
        teachingTimesObj.put("beginMinute",Integer.valueOf(s123.substring(3,5))+5);
        teachingTimesObj.put("code",1);
        teachingTimesObj.put("endHour",newdateTime.substring(11,13));
        teachingTimesObj.put("endMinute",newdateTime.substring(14,16));
        teachingTimesArr.add(teachingTimesObj);
        initScheduleObj.put("teachingTimes",teachingTimesArr);
        initScheduleObj.put("classTypeId",classTypeId);
        initScheduleObj.put("onlineForeignTeacherClassType",false);
        initScheduleObj.put("teachingTimeType","TEACHING_TIME");
        initScheduleObj.put("repeatSchedule",1);
        URI initUrl = new URI(initScheduleUrl);
        log.info("排课接口请求信息："+initScheduleObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(initScheduleObj, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("排课接口返回信息："+repObj.toJSONString());
        return repObj;
    }

    /**
     * 获取课程lessonid
     * @returnapplyClass
     */
    public List<Integer> getClassLessonid(JSONObject lessonidObj) throws URISyntaxException {
        log.info("获取课程lessonid返回信息："+lessonidObj.toJSONString());
        List<Integer> list=new ArrayList<>();
        String lessonIdJsonStr=String.valueOf(lessonidObj.getJSONObject("body"));
        JSONObject dataJson = JSON.parseObject(lessonIdJsonStr);
        String scheduleVoList=String.valueOf(dataJson.getJSONArray("scheduleVoList"));
        JSONArray  resJson = JSONArray.parseArray(scheduleVoList);
        log.info("resJson信息："+resJson);
        for (int i=0;i<resJson.size();i++) {
            JSONObject jsonObj = resJson.getJSONObject(i);
            Integer lessonid = Integer.valueOf(jsonObj.getString("lessonId"));
            list.add(lessonid);
        }
        log.info("lessonids信息是："+list.toString());
        return list;
    }

    /**
     * 上架课程
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject updateClassRelease(Integer classId, JSONObject axgTokenInfo) throws URISyntaxException {
        JSONObject updateClassObj = new JSONObject();
        updateClassObj.put("dtMainClassId",classId);
        updateClassObj.put("classRelease",1);
        URI updateClassUrl = new URI(updateClassReleaseUrl);
        log.info("上架课程接口请求信息："+updateClassObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(updateClassObj, aXGtHttpHeaders(axgTokenInfo), POST, updateClassUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("上架课程接口返回信息："+repObj.toJSONString());
        return repObj;
    }

    /**
     * 主讲审核课程
     * @param appId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject applyPass(Integer appId, LecturerTeacherTokenInfo axgTokenInfo) throws URISyntaxException {
        JSONObject applyPassObj = new JSONObject();
        applyPassObj.put("appId",appId);
        applyPassObj.put("auditRemark","同意");
        URI initUrl = new URI(applyPassUrl);
        log.info("主讲机构审核信息："+applyPassObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(applyPassObj, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("主讲机构审核返回信息："+repObj.toJSONString());
        return repObj;
    }

    /**
     * 查询子课程的subClassId
     * @param appId
     * @param axgTokenInfo
     * @return
     */
    public Integer queryDetails(Integer appId, JSONObject axgTokenInfo,int countNow) throws URISyntaxException {
        String newQueryDetailUrl=queryDetailsUrl
                +"?applyBuyId="+appId;
        log.info("查询子课程的subClassId信息："+newQueryDetailUrl);
        URI initUrl = new URI(newQueryDetailUrl);
        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        JSONObject repBodyObj = repObj.getJSONObject("body");
        JSONObject applyBuyClassObj = repBodyObj.getJSONObject("applyBuyClassInfoVo");
        JSONArray lockaidouRecordListArrayObj = applyBuyClassObj.getJSONArray("lockaidouRecordList");
        log.info("查询子课程的subClassId返回信息4："+lockaidouRecordListArrayObj);
        JSONObject lockaidouRecordListObj  = lockaidouRecordListArrayObj.getJSONObject(countNow);

        Integer subClassIdStr=Integer.valueOf(lockaidouRecordListObj.getString("subClassId"));
        return subClassIdStr;
    }

    /**
     * 创建Ai好课
     * @param basicAiInfo
     * @param axgTokenInfo
     * @return
     */
    public JSONObject createAiClass(JSONObject basicAiInfo, JSONObject axgTokenInfo) throws URISyntaxException {
        URI initUrl = new URI(createAiClassUrl);
        log.info("爱笑管创建AI课请求信息："+basicAiInfo.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(basicAiInfo, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("爱笑管创建AI课返回信息："+repObj.toJSONString());
        String classId = repObj.getString("body");
        //保存创建Ai好课
        marketSetting(classId,axgTokenInfo);
        completionUrl(classId,axgTokenInfo);
        dtMarketSetting(classId,axgTokenInfo);
        return repObj;
    }

    /**
     * 保存创建Ai好课,营销设置保存
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject marketSetting(String classId, JSONObject axgTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl=marketSettingUrl
                +"?classId="+classId;
        log.info("保存创建Ai好课信息："+newQueryDetailUrl);
        URI initUrl = new URI(newQueryDetailUrl);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("保存创建Ai好课信息返回信息："+repObj.toJSONString());
        return repObj;
    }
    /**
     * 各模块完成情况
     * @param classId
     * @param axgTokenInfo
     * @return
     */
    public JSONObject completionUrl(String classId, JSONObject axgTokenInfo) throws URISyntaxException {
        String newQueryDetailUrl=completionUrl
                +"?classId="+classId;
        log.info("各模块完成情况信息："+newQueryDetailUrl);
        URI initUrl = new URI(newQueryDetailUrl);

        RequestEntity requestEntity = new RequestEntity<>(aXGtHttpHeaders(axgTokenInfo), GET, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("各模块完成情况返回信息："+repObj.toJSONString());
        return repObj;
    }
    /**
     * 营销设置回显
     * @param classId
     * @param axgTokenInfo
     * @return
     */

    public JSONObject dtMarketSetting(String classId, JSONObject axgTokenInfo) throws URISyntaxException {

        JSONObject settingObj = new JSONObject();
        settingObj.put("access",0);
        settingObj.put("accessLimit",false);
        settingObj.put("availableTime",timeUtil.getCurrentDateString()+"T16:00:00.000Z");
        settingObj.put("classId",classId);
        settingObj.put("countNum",50);
        settingObj.put("hotSaleFlag","0");
        settingObj.put("mallLink","");
        settingObj.put("qrCode","");
        settingObj.put("saleDate","");

        URI initUrl = new URI(dtMarketSettingUrl);
        log.info("创建直播课请求信息："+settingObj.toJSONString());
        RequestEntity requestEntity = new RequestEntity<>(settingObj, aXGtHttpHeaders(axgTokenInfo), POST, initUrl);
        ResponseEntity responseEntity = restTemplate.exchange(requestEntity,JSONObject.class);
        JSONObject repObj = (JSONObject) responseEntity.getBody();
        log.info("创建直播课返回信息："+repObj.toJSONString());
        return repObj;

    }

}
