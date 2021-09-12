package com.aixuexi.ss.common.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author wangyangyang
 * @date 2020/9/13 23:06
 * @description
 **/
public class SendEmail {
    public void SendFileEmail(String emailcontent)throws Exception{
        try {

            LOG.log("发送邮件通知");
            Properties properties = new Properties();
            // 开启debug调试 ，打印信息
            properties.setProperty("mail.debug", "false");
            // 发送服务器需要身份验证
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.port", "465");// 端口号
            // 发送邮件协议名称
            properties.setProperty("mail.transport.protocol", "smtp");
            // 设置邮件服务器主机名
            properties.setProperty("mail.host", "mail.aixuexi.com/");
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(false);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);
            // 环境信息
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 在session中设置账户信息，Transport发送邮件时会使用
                    return new PasswordAuthentication( "wangyangyang0", "@&gaosi123");//"1576486459@qq.com", "emlccibgvmrfbagg"
                }
            });
            //邮件
            MimeMessage msg = new MimeMessage(session);
            //设置主题
            msg.setSubject("通过我的邮件函数发出的邮件");
            //发件人，注意中文的处理
            msg.setFrom(new InternetAddress("wangyangyang0@aixuexi.com"));

            //整封邮件的MINE消息体
            MimeMultipart msgMultipart = new MimeMultipart("mixed");//混合的组合关系
            //设置邮件的MINE消息体
            msg.setContent(msgMultipart);

            //附件1
            MimeBodyPart attch1 = new MimeBodyPart();
            //正文内容
            MimeBodyPart content = new MimeBodyPart();

            //把内容，附件1加入到 MINE消息体中
            msgMultipart.addBodyPart(attch1);
            msgMultipart.addBodyPart(content);

            //数据源
            //DataSource ds1 = new FileDataSource(new File(logpath));
            //数据处理器
            //DataHandler dh1 = new DataHandler(ds1 );
            //设置第一个附件的数据
            //attch1.setDataHandler(dh1);
            //设置第一个附件的文件名
            //attch1.setFileName("crashlog.txt");
            //设置内容为正文
            content.setContent(emailcontent,"text/html;charset=utf-8");

            msg.setRecipient(Message.RecipientType.TO, new InternetAddress("wangyangyang0@aixuexi.com"));
            //生成文件邮件
            msg.saveChanges();
            //发送邮件
            Transport.send(msg, msg.getAllRecipients() );
        } catch (Exception e) {
            // TODO: handle exception
            LOG.log(e);
        }
    }
}
