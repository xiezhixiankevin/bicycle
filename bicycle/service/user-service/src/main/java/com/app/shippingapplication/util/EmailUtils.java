package com.app.shippingapplication.util;

/**
 * <Description> EmailUtils
 *
 * @author 26802
 * @version 1.0
 * @see com.xzx.shippingapplication.common.util
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    //注入配置文件中配置的信息——>from
    @Value("${spring.mail.from}")
    private String from;

    public  void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //发件人
        message.setFrom(from);
        //收件人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        //发送邮件
        javaMailSender.send(message);
    }


}

