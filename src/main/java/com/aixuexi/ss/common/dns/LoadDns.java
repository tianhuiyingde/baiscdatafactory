package com.aixuexi.ss.common.dns;

import com.google.common.collect.Maps;
import io.leopard.javahost.JavaHost;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


/**
 * @author wangyangyang
 * @date 2020/10/27 11:31
 * @description DNS域名解析
 **/
@Slf4j
public class LoadDns {
    private volatile static LoadDns instance = null;
    private final static String HOSTPROPERTIES = "/test_ghost/host.properties";
    private final static String ENVPROPERTIES = "/test_ghost/env.properties";

    private static int loadDns(Properties properties) {
        return JavaHost.updateVirtualDns(properties);
    }

    private static int loadDns(Map<String, String> hostMap) throws SecurityException {
        if (hostMap != null) {
            int count = JavaHost.updateVirtualDns(hostMap);
            return count;
        } else {
            return 0;
        }
    }

    private LoadDns(Integer environmentTag) {
        try {
            int count = 0;
            if (environmentTag == 1) {
                Properties envPerties = PropertiesUtils.getProperties(ENVPROPERTIES);
                String domain = envPerties.getProperty("domain");
                String host = envPerties.getProperty("host");
                String url = host.concat(domain);

                Map<String, String> hostMapByUrl = getHostMapByUrl(url);
                count = loadDns(hostMapByUrl);
            }else {
                Properties hostProperties = PropertiesUtils.getProperties(HOSTPROPERTIES);
                String domain = hostProperties.getProperty("domain");
                String host = hostProperties.getProperty("host");
                String url = host.concat(domain);

                Map<String, String> hostMapByUrl = getHostMapByUrl(url);
                count = loadDns(hostMapByUrl);
            }
            /*else if (hostProperties.size() > 0){
                count = loadDns(hostProperties);
            }*/
            /* else {
                Map<String, String> hostMap = new HashMap<String, String>();
                hostMap.put("192.168.0.80", "ss.aixuexi.com");
                count = loadDns(hostMap);
            }*/
            log.info("已加载{}个host配置", count);
        } catch (Exception e) {
            throw new IllegalStateException("dns 加载出错，请检查host配置");
        }
    }

    private Map<String, String> getHostMapByUrl(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
                .hostnameVerifier(new TrustAllHostnameVerifier()).build();
        Request build = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(build);
        HashMap<String, String> hostMap = Maps.newHashMap();
        try {
            Response execute = call.execute();
            if (!Objects.isNull(execute.body())) {
                log.info("url3" + url);
                String host = execute.body().string();
                String[] split = host.split("\r\n");
                log.info("hostMapByUrl+host:" + host);
                for (String data : split) {
                    int i = 0;
                    int j = 0;
                    String[] hostSplit = data.split(" ");
                    while (hostSplit[i].equals("") || hostSplit[i].equals(null)) {
                        i++;
                    }
                    j = i;
                    i++;
                    while (hostSplit[i].equals("") || hostSplit[i].equals(null)) {
                        i++;
                    }
                    hostMap.put(hostSplit[i], hostSplit[j]);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return hostMap;

    }

    public static synchronized void initDns(Integer environmentTag) {
        if (instance == null) {
            synchronized (LoadDns.class) {
                if (instance == null) {
                    instance = new LoadDns(environmentTag);
                }
            }
        }
    }

    /**
     * 默认信任所有的证书
     * @return
     */
    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }
   /* private static class TrustAllCerts implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }*/
}






