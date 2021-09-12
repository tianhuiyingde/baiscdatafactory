package com.aixuexi.ss.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

public class JsonHelper {

    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class.getSimpleName());
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonHelper() {
    }

    public static String GetJsonResult(Object src) {
        StringWriter sw = new StringWriter();

        try {
            objectMapper.writeValue(sw, src);
            String var2 = sw.getBuffer().toString();
            return var2;
        } catch (Exception var12) {
            logger.error("GetJsonResult err:", var12);
        } finally {
            sw.flush();

            try {
                sw.close();
            } catch (IOException var11) {
                var11.printStackTrace();
            }

        }

        return "";
    }

    public static Object GetObjByJson(String json, Class type) throws Exception {
        try {
            Object result = objectMapper.readValue(json, type);
            return result;
        } catch (Exception var3) {
            throw new Exception("JsonHelper.GetObjByJson Error:" + var3.getMessage() + var3.getStackTrace());
        }
    }

    public static void SaveJsonFile(Object src, String filePath) {
        try {
            FileWriter fw = new FileWriter(filePath, false);
            objectMapper.writeValue(fw, src);
        } catch (Exception var3) {
            throw new RuntimeException("JsonHelper.SaveJsonFile Error:" + var3.getMessage() + var3.getStackTrace(), var3);
        }
    }

    public static Object ReadJsonFile(String filePath, Class type) {
        try {
            Object result = objectMapper.readValue(new File(filePath), type);
            return result;
        } catch (Exception var3) {
            throw new RuntimeException("JsonHelper.ReadJsonFile Error:" + var3.getMessage() + var3.getStackTrace(), var3);
        }
    }

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
