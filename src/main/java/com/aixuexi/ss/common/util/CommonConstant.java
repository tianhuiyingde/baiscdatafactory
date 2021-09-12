package com.aixuexi.ss.common.util;

import java.util.ArrayList;

/**
 * @author wangyangyang
 * @date 2020/11/11 19:21
 * @description
 **/
public class CommonConstant {
    public static class AES {
        public static final String INSTITUTION_PASSWORD_KEY = "gs_axx_2019_1234";

        public AES() {
        }
    }

    public static void main(String[] args)
    {
//        printREstr();
        isHuiWenStr();

    }

    public static void isHuiWenStr()
    {
        String str = "abcdedcbaxxx";
        char [] arr1 = str.toCharArray();
        int low = 0;
        int high =arr1.length-1;
        for( low=0;low<arr1.length/2;low++)
        {
            System.out.println("low:"+arr1[low]);
            System.out.println("high:"+arr1[high]);
            if(arr1[low]!=arr1[high])
            {
                System.out.println("字符串不是回文串");
                break;
            }
            high--;
        }
    }
    public static void printREstr()
    {
        String str = "abcanbvctfdsdfvsd";
        char[] arr1 = str.toCharArray();
        int len = arr1.length;
        ArrayList outList = new ArrayList<String>();
        boolean flag = false;
        for(int i=0;i<len;i++)
        {
            for(int j =0;j<len;j++)
            {
                if(i!=j)
                {
                    if(arr1[i]==arr1[j] && !outList.contains(arr1[i]) ) {
                        outList.add(arr1[i]);
//                        flag = true;
//                        break;
                    }
                }
            }
//            if(flag)
//            {
//                break;
//            }
        }
        System.out.println(outList.toString());
    }
}
