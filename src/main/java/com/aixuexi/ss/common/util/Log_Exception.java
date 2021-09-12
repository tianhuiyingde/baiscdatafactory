package com.aixuexi.ss.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: wangyangyang
 * @Date: 2020/1/2 20:43
 */
public class Log_Exception {
    /**
     * @将错误信息输入到txt中
     * @param path
     * @throws IOException
     */
    public void writeEror_to_txt(String path,String content) throws IOException{

        File F=new File(path);
        //如果文件不存在,就动态创建文件
        if(!F.exists()){
            F.createNewFile();
        }
        FileWriter fw=null;
        String writeDate="时间:"+this.get_nowDate()+"---"+"error:"+content;
        try {
            //设置为:True,表示写入的时候追加数据
            fw=new FileWriter(F, true);
            //回车并换行
            fw.write(writeDate+"\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw!=null){
                fw.close();
            }
        }

    }
    /**
     * @获取系统当前时间
     * @return
     */
    public  String get_nowDate(){

        Calendar D=Calendar.getInstance();
        int year=0;
        int moth=0;
        int day=0;
        year=D.get(Calendar.YEAR);
        moth=D.get(Calendar.MONTH)+1;
        day=D.get(Calendar.DAY_OF_MONTH);
        String now_date=String.valueOf(year)+"-"+String.valueOf(moth)+"-"+String.valueOf(day);
        return now_date;
    }
    //测试方法
    public static void main(String[] args) throws IOException {
        String path=System.getProperty("user.dir")+"\\file.txt";
        String content = null;
        try{
//            String i="";
//            if(i==null){
//                System.out.println("111");
//                content="hello  你好!天天向上!";
//            }
//            String[] strings={"1","2","3"};
            List<String> list=new ArrayList<>();
            list.add("1");
            list.add("2");
            list.add("3");
            for(String  i:list){
                System.out.println(i);
            }
            String j=list.get(3);
        }catch (Exception e){
            content=e.getClass().getName()+"  error Info  "+e.getMessage();
//            content=e.getMessage();
        }
        Log_Exception le=new Log_Exception();
        le.writeEror_to_txt(path, content);
    }

}
