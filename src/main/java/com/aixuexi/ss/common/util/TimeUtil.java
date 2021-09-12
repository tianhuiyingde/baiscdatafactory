package com.aixuexi.ss.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 时间处理工具类
*/
@Slf4j
@Component
public class TimeUtil {
    /**
     * @param datetime - 时间格式的字符串”YYYY-MM-DD HH:MM:SS”
     * @param second – 加上的时间（秒）
     * @return 返回增加second后的时间格式的字符串
     *
     * 给时间datetime加上时间second.
     * datetime 必须为 ”YYYY-MM-DD HH:MM:SS” ，HH为24小时制标识，如”2004-07-04 09:24:05”
     * second为需要增加的秒数，如果为负数，则为减去。如果datetime不满足时间格式，返回null。如：
     *
     * timeAdd (”2004-07-04 09:24:05”,3600) 返回”2004-07-04 10:24:05”
     * timeAdd (”2004-07-04 09:24:05”,86400) 返回”2004-07-05 09:24:05”
     * timeAdd (”2004-07-04 09:24:05”,-86400) 返回”2004-07-04 09:24:05”
     * timeAdd (”2004-7-4 9:24:05”,-86400) 返回null
     *
     */
    public static String timeAdd(String datetime, int second) {
        Calendar calendar = getCalendar(datetime) ;
        if(calendar == null) return null ;
        calendar.add(Calendar.SECOND, second) ;

        return getTimeString(calendar) ;
    }

    /**
     * @param fromdatetime - 时间格式的字符串”YYYY-MM-DD HH:MM:SS”
     * @param todatetime - 时间格式的字符串”YYYY-MM-DD HH:MM:SS”
     * @return 返回两个时间之间间隔的秒数
     *
     * 计算fromdatetime和todatetime之间相隔的时间（秒）.
     * 如果fromdatetime或者todatetime不满足时间格式，返回0，如：
     *
     * timeInterval (”2004-07-04 09:24:05”, ”2004-07-04 10:24:05”) 返回3600
     * timeInterval (”2004-07-05 09:24:05”, ”2004-07-04 09:24:05”)  返回 -86400
     * timeInterval (”2004-7-5 09:24:05”, ”2004-7-4 09:24:05”)  返回 0
     *
     */
    public static long timeInterval(String fromdatetime, String todatetime) {
        Calendar fromcalendar = getCalendar(fromdatetime) ;
        Calendar tocalendar = getCalendar(todatetime) ;

        if(fromcalendar==null || tocalendar==null) return 0 ;

        return (tocalendar.getTime().getTime() - fromcalendar.getTime().getTime()) / 1000 ;
    }

    /**
     * @param date – 日期格式的字符串”YYYY-MM-DD”
     * @param day – 加上的天数
     * @return 返回增加day后的日期格式的字符串
     *
     * 给日期date加上天数day.
     * date 必须为 ”YYYY-MM-DD” ，如”2004-07-04”，day为需要增加的天数，如果为负数，则为减去。
     * 如果date不满足日期格式，返回null。如：
     *
     * dateAdd (”2004-02-29”,1) 返回”2004-03-01”
     * dateAdd (”2004-07-04”,1) 返回”2004-07-05”
     * dateAdd (”2004-07-04”,-1) 返回”2004-07-03”
     * dateAdd (”2004-7-4”,1)  返回null
     *
     */
    public static String dateAdd(String date, int day) {
        Calendar calendar = getCalendar(date) ;
        if(calendar == null) return null ;
        calendar.add(Calendar.DATE, day) ;

        return getDateString(calendar) ;
    }

    /**
     * @param fromdate - 日期格式的字符串”YYYY-MM-DD”
     * @param todate - 日期格式的字符串”YYYY-MM-DD”
     * @return 返回两个日期之间间隔的天数
     *
     * 计算fromdate和todate之间相隔的天数.
     * 如果fromdate或者todate不满足时间格式，返回0，如：
     *
     * dateInterval (”2004-03-01”, ”2004-02-28”) 返回-2
     * dateInterval (”2004-07-04”, ”2004-07-05”) 返回1
     * dateInterval (”2004-07-05”, ”2004-07-04”)  返回 -1
     * dateInterval (”2004-07-04”, ”2004-07-04”)  返回 0
     * dateInterval (”2004-7-5”, ”2004-7-4”)  返回 0
     *
     */
    public static int dateInterval(String fromdate, String todate) {
        /*Calendar fromcalendar = getCalendar(fromdate) ;
        Calendar tocalendar = getCalendar(todate) ;

        if(fromcalendar==null || tocalendar==null) return 0 ;

        int isascorder = 1 ;        // 表明正负
        int dateinterval = 0 ;
        if(fromcalendar.after(tocalendar)) {
            isascorder = -1 ;
            Calendar tempcalendar = fromcalendar ;
            fromcalendar = tocalendar ;
            tocalendar = tempcalendar ;
        }

        while(fromcalendar.before(tocalendar)) {
            dateinterval ++ ;
            fromcalendar.add(Calendar.DATE,1) ;
        }

        return isascorder*dateinterval ;
        */
    	  Calendar fromcalendar = getCalendar(fromdate);
          Calendar tocalendar = getCalendar(todate);

          if (fromcalendar == null || tocalendar == null)
              return 0;

          return (int) ((tocalendar.getTimeInMillis() - fromcalendar.getTimeInMillis()) / 3600 / 24 / 1000);
    }
    /**
     * @param fromdatetime - 时间格式的字符串”YYYY-MM-DD HH:MM:SS”
     * @param todatetime - 时间格式的字符串”YYYY-MM-DD HH:MM:SS”
     * @return 以"X天X小时X分X秒"的形式返回两时间的差值
     *
     * 如果fromdatetime或者todatetime不满足时间格式，返回0，如：
     *
     *
     * timeInterval2 (”2004-07-04 09:24:05”, ”2004-07-04 10:24:05”) 返回 "1小时0分0秒"
     * timeInterval2 (”2004-07-04 09:24:05”, ”2004-07-05 09:24:05”)  返回 "1天0小时0分0秒"
     * timeInterval2 (”2004-7-5 09:24:05”, ”2004-7-4 09:24:05”)  返回 ""
     *
     */
    public static String timeInterval2(String fromdatetime, String todatetime,int languageid) {
    	String returnStr="";
        long intervaltime=timeInterval(fromdatetime,todatetime);

        if(intervaltime>=0){
            long tmpDay=0;
            long tmpHour=0;
            long tmpMinute=0;
            long tmpSecond=0;

        	tmpDay=intervaltime / 86400;
        	tmpHour=(intervaltime % 86400) / 3600;
        	tmpMinute=(intervaltime % 3600) / 60;
        	tmpSecond=intervaltime % 60;
        	if(languageid==8){
	        	if(tmpDay>0){
	        		returnStr=String.valueOf(tmpDay)+"days "+String.valueOf(tmpHour)+"hours "+String.valueOf(tmpMinute)+"minutes "+String.valueOf(tmpSecond)+"seconds";
	        	}else if(tmpHour>0){
	        		returnStr=String.valueOf(tmpHour)+"hours "+String.valueOf(tmpMinute)+"minutes "+String.valueOf(tmpSecond)+"seconds";
	        	}else if(tmpMinute>0){
	        		returnStr=String.valueOf(tmpMinute)+"minutes "+String.valueOf(tmpSecond)+"seconds";
	        	}else if(tmpSecond>=0){
	        		returnStr=String.valueOf(tmpSecond)+"seconds";
	        	}
        	}else{
	        	if(tmpDay>0){
	        		returnStr=String.valueOf(tmpDay)+"天"+String.valueOf(tmpHour)+"小时"+String.valueOf(tmpMinute)+"分"+String.valueOf(tmpSecond)+"秒";
	        	}else if(tmpHour>0){
	        		returnStr=String.valueOf(tmpHour)+"小时"+String.valueOf(tmpMinute)+"分"+String.valueOf(tmpSecond)+"秒";
	        	}else if(tmpMinute>0){
	        		returnStr=String.valueOf(tmpMinute)+"分"+String.valueOf(tmpSecond)+"秒";
	        	}else if(tmpSecond>=0){
	        		returnStr=String.valueOf(tmpSecond)+"秒";
	        	}
        	}
        }

        return returnStr;
    }

    /**
     * @param date – 日期格式的字符串”YYYY-MM-DD”
     * @return 返回日期date 对应的星期
     *
     * 日期date 对应的星期.
     * 星期的标识如下：
     * 0：星期天
     * 1：星期一
     * 2：星期二
     * 3：星期三
     * 4：星期四
     * 5：星期五
     * 6：星期六
     *
     * 如果date不满足日期格式，返回-1，如：
     *
     * dateWeekday (”2004-02-28”) 返回6
     * dateWeekday (”2004-07-04”) 返回0
     * dateWeekday (”2004-07-19”) 返回1
     * dateWeekday (”2004-7-4”)  返回-1
     *
     */
    public static int dateWeekday(String date) {
        Calendar calendar = getCalendar(date) ;
        if(calendar == null) return -1 ;
        return calendar.get(Calendar.DAY_OF_WEEK) - 1 ;
    }
    /**
     * @param date – 日期格式的字符串”YYYY-MM-DD”
     * @return 返回当前天
     *
     * 日期date 对应的号.

     *
     */
    public static int dateMonthday(String date) {
        Calendar calendar = getCalendar(date) ;
        if(calendar == null) return -1 ;
        return calendar.get(Calendar.DAY_OF_MONTH) ;
    }
    /**
     * @return 返回当前时间字符，格式为 yyyy'-'MM'-'dd' 'HH:mm:ss
     *
     * 返回当前时间字符，默认格式为yyyy'-'MM'-'dd' 'HH:mm:ss
     *
     * 如 2004-09-07 23:20:05
     */
    public static String getCurrentTimeString() {
        String timestrformart = "yyyy'-'MM'-'dd' 'HH:mm:ss" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;
        Calendar calendar = Calendar.getInstance() ;

        return SDF.format(calendar.getTime()) ;
    }

    /**
     * @return 返回当前时间字符，格式为 yyyy'-'MM'-'dd
     *
     * 返回当前时间字符，默认格式为yyyy'-'MM'-'dd
     *
     * 如 2004-09-07
     */
    public static String getCurrentDateString() {
        String timestrformart = "yyyy'-'MM'-'dd" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;
        Calendar calendar = Calendar.getInstance() ;

        return SDF.format(calendar.getTime()) ;
    }

        /**
     * @return 返回当前日历的时间字符，格式为 HH:mm:ss
     */
    public static String getOnlyCurrentTimeString() {
        String timestrformart = "HH:mm:ss" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;
        Calendar calendar = Calendar.getInstance() ;
        return SDF.format(calendar.getTime()) ;
    }
    /**
     * @param date - 日期
     * @return 返回给定日期的时间字符，格式为 yyyy'-'MM'-'dd' 'HH:mm:ss
     */
    public static String getTimeString(Date date) {
        String timestrformart = "yyyy'-'MM'-'dd' 'HH:mm:ss" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;

        return SDF.format(date) ;
    }

    /**
     * @param calendar - 日历
     * @return 返回给定日历的时间字符，格式为 yyyy'-'MM'-'dd' 'HH:mm:ss
     */
    public static String getTimeString(Calendar calendar) {
        String timestrformart = "yyyy'-'MM'-'dd' 'HH:mm:ss" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;

        return SDF.format(calendar.getTime()) ;
    }

    /**
     * @param date - 日期
     * @return  返回给定日期的日期字符，格式为 yyyy'-'MM'-'dd
     */
    public static String getDateString(Date date) {
        String timestrformart = "yyyy'-'MM'-'dd" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;

        return SDF.format(date) ;
    }

    /**
     * 将字符串日期转换成指定格式的java.lang.Date类型
     * @param date	字符串日期
     * @param format	格式化指定的类型
     * @return
     */
    public static Date getString2Date(String strDate, String format) {
    	Date date = null;
    	try {
    		date = new SimpleDateFormat(format).parse(strDate);
    	}
    	catch(Exception e) {
    		date = null;
    	}
        return date;
    }

    /**
     * @param calendar  - 日历
     * @return 返回给定日历的日期字符，格式为 yyyy'-'MM'-'dd
     */
    public static String getDateString(Calendar calendar) {
        String timestrformart = "yyyy'-'MM'-'dd" ;
        SimpleDateFormat SDF = new SimpleDateFormat(timestrformart) ;

        return SDF.format(calendar.getTime()) ;
    }

    /**
     * @param date - 日期
     * @param formart - 输出格式
     * @return 返回给定日期的字符，格式为formart ，如果格式不正确，返回""
     */
    public static String getFormartString(Date date , String formart) {
        SimpleDateFormat SDF = new SimpleDateFormat(formart) ;
        return SDF.format(date) ;
    }

    /**
     * @param calendar - 日历
     * @param formart - 输出格式
     * @return 返回给定日历的字符，格式为formart ，如果格式不正确，返回""
     */
    public static String getFormartString(Calendar calendar , String formart) {
        SimpleDateFormat SDF = new SimpleDateFormat(formart) ;
        return SDF.format(calendar.getTime()) ;
    }


    /**
     * @param datetime - 给定的日期时间，格式为 '2004-05-12 12:00:23'  或者 '2004-05-12'
     * @return 返回给定日历， 如果格式不正确，返回null
     */
    public static Calendar getCalendar(String datetime) {
        int datetimelength = datetime.length() ;

        switch(datetimelength) {
            case 19 :
                return getCalendar(datetime , "yyyy'-'MM'-'dd' 'HH:mm:ss") ;
            case 10 :
                return getCalendar(datetime , "yyyy'-'MM'-'dd") ;
            default :
                return null ;
        }

    }


    /**
     * @param datetime - 给定的日期时间
     * @param formart - 给定的日期时间的格式
     * @return 返回给定日历， 如果格式不正确，返回null
     */
    public static Calendar getCalendar(String datetime, String formart) {
        SimpleDateFormat SDF = new SimpleDateFormat(formart) ;

        Calendar calendar = Calendar.getInstance() ;
        try {
            calendar.setTime(SDF.parse(datetime)) ;
        } catch (ParseException e) {
            return null ;
        }

        return calendar ;
    }


	public static String getCurrentSeason(){
		String m = getFormartString(Calendar.getInstance(),"MM");
		String strSeason = "";

		if(m.equals("01") || m.equals("02") || m.equals("03")){
			strSeason = "1";
		}else if(m.equals("04") || m.equals("05") || m.equals("06")){
			strSeason = "2";
		}else if(m.equals("07") || m.equals("08") || m.equals("09")){
			strSeason = "3";
		}else if(m.equals("10") || m.equals("11") || m.equals("12")){
			strSeason = "4";
		}
		return strSeason;
	}
	   /**
     * @param datetime - 给定的日期
     * @param formart - 给定的日期时间的格式
     * @return 返回给定日期的月有几个周一(近似于一个月的周数)
     */
    public static int getWeeksOfMonth(String years,int months) {
    	int s=0;
    	int days=30;
    	String datetime="";
    	String month="";
    	month=months<10?"0"+months:""+months;
    	datetime=years+"-"+month+"-"+"01";
    	Calendar calendar = getCalendar(datetime) ;
    	days=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    	for (int i=0;i<days;i++)
    	{String tempmonth=((i+1)<10)?"0"+(i+1):""+(i+1);
    	String tempDay=years+"-"+month+"-"+tempmonth;
    	if (dateWeekday(tempDay)==1) s++;
    	}
        return s ;
    }

    /**
    * 得到某一年周的总数
    *
    * @param year
    * @return
    */
    public static int getMaxWeekNumOfYear(int year) {
    Calendar c = new GregorianCalendar();
    c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

    return getWeekOfYear(c.getTime());
    }

    /**
    * 取得当前日期是多少周
    *
    * @param date
    * @return
    */
    public static int getWeekOfYear(Date date) {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setMinimalDaysInFirstWeek(7);
    c.setTime (date);

    return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得当前日期是多少周
     *
     * @param date
     * @return
     */
     public static int getWeekOfYear(Date date, int firstDay) {
     Calendar c = new GregorianCalendar();
     c.setFirstDayOfWeek(firstDay);
     c.setMinimalDaysInFirstWeek(7);
     c.setTime (date);

     return c.get(Calendar.WEEK_OF_YEAR);
     }

    /**
    * 得到某年某周的第一天
    *
    * @param year
    * @param week
    * @return
    */
    public static Date getFirstDayOfWeek(int year, int week) {
    Calendar c = new GregorianCalendar();
    c.set(Calendar.YEAR, year);
    c.set (Calendar.MONTH, Calendar.JANUARY);
    c.set(Calendar.DATE, 1);

    Calendar cal = (GregorianCalendar) c.clone();
    cal.add(Calendar.DATE, week * 7);

    return getFirstDayOfWeek(cal.getTime ());
    }

    /**
    * 得到某年某周的最后一天
    *
    * @param year
    * @param week
    * @return
    */
    public static Date getLastDayOfWeek(int year, int week) {
    Calendar c = new GregorianCalendar();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, Calendar.JANUARY);
    c.set(Calendar.DATE, 1);

    Calendar cal = (GregorianCalendar) c.clone();
    cal.add(Calendar.DATE , week * 7);

    return getLastDayOfWeek(cal.getTime());
    }

    /**
    * 取得当前日期所在周的第一天
    *
    * @param date
    * @return
    */
    public static Date getFirstDayOfWeek(Date date) {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setTime(date);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
    return c.getTime ();
    }

    /**
    * 取得当前日期所在周的最后一天
    *
    * @param date
    * @return
    */
    public static Date getLastDayOfWeek(Date date) {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setTime(date);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
    return c.getTime();
    }

    /**
     * 取得当前日期所在周的第几天
     *
     * @param date
     * @return
     */
     public static int getDayOfWeek(String strDate) {
      SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
      Date date=null;
      try {
    	  date=dateFormat.parse(strDate);
	  } catch (Exception e) {
			// TODO: handle exception
	  }
      return date.getDay();
     }

    public static String SetDateFormat(String myDate,String strFormat) throws ParseException{

        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        String sDate = sdf.format(sdf.parse(myDate));

        return sDate;
    }


    /*****************************************
     * @功能     计算某年某月的开始日期
     * @return  interger
     * @throws ParseException
     * @throws ParseException
     ****************************************/
    public static String getYearMonthFirstDay(int yearNum,int monthNum) throws ParseException{

     //分别取得当前日期的年、月、日
     String tempYear = Integer.toString(yearNum);
     String tempMonth = Integer.toString(monthNum);
     String tempDay = "1";
     String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
     return SetDateFormat(tempDate,"yyyy-MM-dd");

    }

    /*****************************************
     * @功能     判断某年是否为闰年
     * @return  boolean
     * @throws ParseException
     ****************************************/
    public static boolean isLeapYear(int yearNum){
     boolean isLeep = false;
        /**判断是否为闰年，赋值给一标识符flag*/
        if((yearNum % 4 == 0) && (yearNum % 100 != 0)){
         isLeep = true;
        }  else if(yearNum % 400 ==0){
         isLeep = true;
        } else {
         isLeep = false;
        }
        return isLeep;
    }

    /*****************************************
     * @功能     计算某年某月的结束日期
     * @return  interger
     * @throws ParseException
     ****************************************/
    public static String getYearMonthEndDay(int yearNum,int monthNum) throws ParseException {


        //分别取得当前日期的年、月、日
      String tempYear = Integer.toString(yearNum);
      String tempMonth = Integer.toString(monthNum);
      String tempDay = "31";
      if (tempMonth.equals("1") || tempMonth.equals("3") || tempMonth.equals("5") || tempMonth.equals("7") ||tempMonth.equals("8") || tempMonth.equals("10") ||tempMonth.equals("12")) {
       tempDay = "31";
      }
      if (tempMonth.equals("4") || tempMonth.equals("6") || tempMonth.equals("9")||tempMonth.equals("11")) {
       tempDay = "30";
      }
      if (tempMonth.equals("2")) {
       if (isLeapYear(yearNum)) {
        tempDay = "29";
       } else {
          tempDay = "28";
       }
      }
      //System.out.println("tempDay:" + tempDay);
      String tempDate = tempYear + "-" +tempMonth + "-" + tempDay;
      return SetDateFormat(tempDate,"yyyy-MM-dd");

     }
    /**
     * @param year 当前年份
     * @param month 当前月份
     * @param type 最大日期类型。0，当前年；1，当前月；2，当前季度
     * @return
     */
    public static int getMaxDays(int year, int month, int type){
    	int maxDays = 0;
    	boolean isLeapYear = isLeapYear(year);
    	if(type == 0){
    		if(isLeapYear == true){
    			maxDays = 366;
    		}else{
    			maxDays = 365;
    		}
    	}else if(type == 1){
    		if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
    			maxDays = 31;
    		}else if(month==4 || month==6 || month==9 || month==11){
    			maxDays = 30;
    		}else if(month == 2){
    			if(isLeapYear == true){
        			maxDays = 29;
        		}else{
        			maxDays = 28;
        		}
    		}
    	}else if(type == 2){
    		if(month==1 || month==2 || month==3){
    			if(isLeapYear == true){
        			maxDays = 91;
        		}else{
        			maxDays = 90;
        		}
    		}else if(month==4 || month==5 || month==6){
    			maxDays = 91;
    		}else if(month==7 || month==8 || month==9){
    			maxDays = 92;
    		}else if(month==10 || month==11 || month==12){
    			maxDays = 92;
    		}
    	}
    	return maxDays;
    }

    /**
	 * 根据月份获得所在季度月份
	 */
	public static String getQuarterMonth(int month){
		String reslut ="";
		if(1==month||2==month||3==month){
			reslut = "(1,2,3)";
		}
		if(4==month||5==month||6==month){
			reslut = "(4,5,6)";
		}
		if(7==month||8==month||9==month){
			reslut = "(7,8,9)";
		}
		if(10==month||11==month||12==month){
			reslut = "(10,11,12)";
		}
		return reslut;
	}

	/**
	 * 获取本月第一天
	 * @param args
	 */
	public static String getMonthBeginDay(){
		String result = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = (Date) cal.getTime();
        result = datef.format(beginTime);
		return result;
	}

	/**
	 * 获取上个月第一天
	 * @param args
	 */
	public static String getLastMonthBeginDay(){
		String result = "";
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = (Date) cal.getTime();
        result = datef.format(beginTime);
		return result;
	}

	/**
	 * 获取本月最后一天
	 * @param args
	 */
	public static String getMonthEndDay(){
		String result = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
	     cal.set( Calendar.DATE, 1 );
         cal.roll(Calendar.DATE, - 1 );
         Date endTime = (Date) cal.getTime();
         result = datef.format(endTime);
		return result;
	}

	/**
	 * 获取上个月最后一天
	 * @param args
	 */
	public static String getLastMonthEndDay(){
		String result = "";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		cal.set(Calendar.DAY_OF_MONTH,0);
         Date endTime = (Date) cal.getTime();
         result = datef.format(endTime);
		return result;
	}

	/**
	 * 获取本月第一天
	 * @param args
	 */
	public static String getMonthBeginDay(String date){
		String result = "";
		Calendar cal = getCalendar(date);
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = (Date) cal.getTime();
        result = datef.format(beginTime);
		return result;
	}
	/**
	 * 获取本月最后一天
	 * @param args
	 */
	public static String getMonthEndDay(String date){
		String result = "";
		Calendar cal = getCalendar(date);
		SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
	     cal.set( Calendar.DATE, 1 );
         cal.roll(Calendar.DATE, - 1 );
         Date endTime = (Date) cal.getTime();
         result = datef.format(endTime);
		return result;
	}

	/**
	 * 根据参数，返回特定的日期
	 * @param
	 */
	public static String getDateByOption(String type,String soe){
		if(type.equals("")||type.equals("0"))return "";
		if(type.equals("1")){//今天
			return getCurrentDateString();
		}
		if(soe.equals("0")){//开始日期
			if(type.equals("2")){//本周
				return getDateString(getFirstDayOfWeek(new Date()));
			}else if(type.equals("3")){//本月
				return getMonthBeginDay();
			}else if(type.equals("7")){//上个月
				return getLastMonthBeginDay();
			}else if(type.equals("4")){//本季
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		        int month = getQuarterInMonth(cal.get(Calendar.MONTH), true);
		        int currentMonth=cal.get(Calendar.MONTH);
		        if(currentMonth<1) cal.add(Calendar.YEAR,-1);
		        cal.set(Calendar.MONTH, month);
		        cal.set(Calendar.DAY_OF_MONTH, 1);
		        return datef.format((Date)cal.getTime());
			}else if(type.equals("5")){//本年
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
				cal.set(Calendar.MONTH, Calendar.JANUARY);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				return datef.format((Date)cal.getTime());
			}else if(type.equals("8")){//上一年
				return getFirstDayOfLastYear();
			}
		}else{
			if(type.equals("2")){//本周
				return getDateString(getLastDayOfWeek(new Date()));
			}else if(type.equals("3")){//本月
				return getMonthEndDay();
			}else if(type.equals("7")){//上个月
				return getLastMonthEndDay();
			}else if(type.equals("4")){//本季
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
		        int month = getQuarterInMonth(cal.get(Calendar.MONTH), false);
		        int currentMonth=cal.get(Calendar.MONTH);
		        if(currentMonth<1) cal.add(Calendar.YEAR,-1);

		        cal.set(Calendar.MONTH, month+1);
		        cal.set(Calendar.DAY_OF_MONTH, 0);
		        return datef.format((Date)cal.getTime());
			}else if(type.equals("5")){//本年
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat datef=new SimpleDateFormat("yyyy-MM-dd");
				cal.set(Calendar.MONTH, Calendar.DECEMBER+1);
				cal.set(Calendar.DAY_OF_MONTH, 0);
				return datef.format((Date)cal.getTime());
			}else if(type.equals("8")){//上一年
				return getEndDayOfLastYear();
			}
		}
		return "";
	}

	/**
	 *获取一周的开头的日期
	 * @return
	 */
	public static String getWeekBeginDay(){
		String result = "";
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //获取本周一的日期
        result = df.format(cal.getTime());
		return result;
	}

	/**
	 *获取一周的开头的日期
	 * @return
	 */
	public static String getWeekBeginDay(String date){
		String result = "";
		Calendar cal =getCalendar(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); //获取本周一的日期
        result = df.format(cal.getTime());
		return result;
	}

	/**
	 * 获取一周结束的日期
	 * @return
	 */
	public static String getWeekEndDay(){
		String result = "";
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		result = df.format(cal.getTime());
		return result;
	}

	/**
	 * 获取一周结束的日期
	 * @return
	 */
	public static String getWeekEndDay(String date){
		String result = "";
		Calendar cal =getCalendar(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		result = df.format(cal.getTime());
		return result;
	}

	// 返回第几个月份，不是几月
    // 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
    private static int getQuarterInMonth(int month, boolean isQuarterStart) {
    	month = month+1;
        int months[] = { 0, 3, 6, 9 };
        if (!isQuarterStart) {
            months = new int[] { 2, 5, 8, 11 };
        }
        if (month >= 1 && month <= 3)
            return months[0];
        else if (month >= 4 && month <= 6)
            return months[1];
        else if (month >= 7 && month <= 9)
            return months[2];
        else
            return months[3];
    }
		/**
	 * 获取本年度第一天
	 *
	 * @return
	 */
	public static String getFirstDayOfTheYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_YEAR, 1);
		return format.format(gc.getTime());

	}

	/**
	 * 获取上年度第一天
	 *
	 * @return
	 */
	public static String getFirstDayOfLastYear() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.add(Calendar.YEAR, -1);
		gc.set(Calendar.DAY_OF_YEAR, 1);
		return format.format(gc.getTime());

	}

	/**
	 * 获取本年度最后一天
	 * @return
	 * @throws ParseException
	 */
	public  static String  getLastDayOfYear() throws ParseException
	{
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		int year=gc.get(Calendar.YEAR);
		String dateinfo=getYearMonthEndDay(year,11);
	    return dateinfo+" 23:59:59";

	}

	/**
	 * 获取上年度最后一天
	 * @return
	 * @throws ParseException
	 */
	public  static String  getEndDayOfLastYear()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_YEAR, 0);
		return format.format(gc.getTime());

	}

	/**
	 * 获取当前季度的第一天
	 *
	 * @return
	 */
	public static String getFirstDayOfSeason() {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		int month = gc.get(Calendar.MONTH) + 1;
		int year = gc.get(Calendar.YEAR);
		String quarter = "01";
		if (month >= 1 && month <= 3) {
			quarter = "01";
		}
		if (month >= 4 && month <= 6) {
			quarter = "04";
		}
		if (month >= 7 && month <= 9) {
			quarter = "07";
		}
		if (month >= 10 && month <= 12) {
			quarter = "10";
		}
		return year + "-" + quarter + "-01";
	}
	/**
	 * 获取当前季度的最后一天
	 *
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayDayOfSeason() throws ParseException {
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		int month = gc.get(Calendar.MONTH) + 1;
		int year = gc.get(Calendar.YEAR);
		int monthuse=3;
		if (month >= 1 && month <= 3) {
			monthuse = 3;
		}
		if (month >= 4 && month <= 6) {
			monthuse = 6;
		}
		if (month >= 7 && month <= 9) {
			monthuse = 9;
		}
		if (month >= 10 && month <= 12) {
			monthuse = 12;
		}
		String dateinfo=getYearMonthEndDay(year,monthuse);
		return  dateinfo+" 23:59:59";
	}




    /**
     * 获取本月第一天
     * @return
     */
	public  static  String  getFirstDayOfMonth()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_MONTH, 1);
		return format.format(gc.getTime());
	}
	/**
	 * 获取本月最后一天
	 * @return
	 * @throws ParseException
	 */
	public  static  String  getLastDayOfMonth() throws ParseException
	{
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		int year=gc.get(Calendar.YEAR);
		int month=gc.get(Calendar.MONTH);
		String dateinfo=getYearMonthEndDay(year,month+1);
		return dateinfo+" 23:59:59";
	}

	  /**
     * 获取本周第一天
     * @return
     */
	public  static  String  getFirstDayOfWeek()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.set(Calendar.DAY_OF_WEEK, 2);
		return format.format(gc.getTime());
	}
	/**
	 * 获取本周最后一天
	 * @return
	 */
	public  static  String  getLastDayOfWeek()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(new Date());
		gc.add(Calendar.WEEK_OF_MONTH, 1);
		gc.set(Calendar.DAY_OF_WEEK, 1);
		return format.format(gc.getTime())+" 23:59:59";
	}
	/**
	 * 获取今天日期
	 * @return
	 */
	public  static String  getToday()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());

	}

	/**
	 * 获取今天在本月的第几天
	 * @return
	 */
	public  static String  getDayOfMonth()
	{
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		int day=gc.get(Calendar.DAY_OF_MONTH);
		return day+"";

	}

    /**
     * 获取当月天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(String date) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 比较两个时间大小
     * begin<=enddate = true
     * begin>enddate = false
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean comparetoTime(String beginTime, String endTime) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date bt = sdf.parse(beginTime);
        Date et = sdf.parse(endTime);
        if (bt.before(et))
        {
            //表示bt小于et
            return true;
        }
        else
        {
            //--反之
            if (beginTime.equals(endTime))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    /**获取小数点位数
     *
     * @param v
     * @param i
     * @return
     */
    //取小数点后第几位
    public static String getPointValue(String v) {
        return getPointValue(v, 2);
    }
    public static String getPointValue(String v, int i)
    {
        return getPointValue(v, 2, "-1");
    }
    public static String getPointValue(String v, int i, String k) {
        try {
            Double.parseDouble(v);
            String temp = v;
            if (v.indexOf("E") != -1) {
                temp = getfloatToString(v);
            }
            if (temp.indexOf(".") == -1) {
                temp = temp + ".";
                for (int j = 0; j < i; j++)
                    temp = temp + "0";
            } else if ((temp.length() - temp.lastIndexOf(".")) <= i) {
                for (int j = 0; j < (i - temp.length() + temp.lastIndexOf(".") + 1); j++)
                    temp = temp + "0";
            } else {
                temp = temp.substring(0, temp.lastIndexOf(".") + i + 1);
            }
            return temp;
        } catch (Exception e) {
            return k;
        }
    }
    public static Float getPointValue(Float v)
    {
        return Float.parseFloat(getPointValue(String.valueOf(v), 2));
    }

    public static String getfloatToString(String value) {
        boolean isNegNum =false;
        if(value.indexOf("-")!=-1){  //td49359 by sjh 当传入的数据位负数时需先去负号，返回时需添加上负号
            isNegNum=true;
            value=value.substring(1,value.length());
        }
        int index = value.indexOf("E");
        if (index == -1)
            return value;

        int num = Integer.parseInt(value.substring(index + 1, value.length()));
        value = value.substring(0, index);
        index = value.indexOf(".");
        value = value.substring(0, index) + value.substring(index + 1, value.length());
        String number = value;
        if (value.length() <= num) {
            for (int i = 0; i < num - value.length()+1; i++) { //td49359 by sjh 长度取值应该是取小数点后的数值长度而不是包含小数点前的数值
                number += "0";
            }
        } else {
            number = number.substring(0, num + 1) + "." + number.substring(num + 1) + "0";
        }
        if(isNegNum)number="-"+number;
        return number;
    }

//    /**
//     * 获取当月工作日----地平线
//     * @param dateStr
//     * @return
//     */
//    public  int getWorkDays(String dateStr)
//    {
//        int workDays = 24;
//        try{
//            String repStr = workDaysService.getWorkDaysByDate(dateStr);
//            JSONObject repObj = JSON.parseObject(repStr);
//            workDays = (int) repObj.get("workDays");
//        }
//        catch (Exception e)
//        {
//            e.getMessage();
//            log.error(e.getMessage());
//        }
//            return workDays;
//    }


}
