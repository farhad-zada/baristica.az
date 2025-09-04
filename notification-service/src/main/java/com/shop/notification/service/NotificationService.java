package com.shop.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.shop.common.event.OrderEvent;
import com.shop.common.event.OrderStatus;
import com.shop.notification.exception.UnexpectedOrderStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    private JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String subject, String text) {
         try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("farhad.szd@gmail.com");
            message.setTo("panworldist@gmail.com");
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOrderMail(OrderEvent event) {
        String subject;
        String text;
        if (OrderStatus.COMPLETED.equals(event.getStatus())) {
            subject = "NEBULA: ORDER COMPLETED SUCCESSFULLY";
            text = "Your order have been completed successfully. We will deliver it as soon as possible";
        } else if (OrderStatus.CANCELLED.equals(event.getStatus())) {
            subject = "NEBULA: ORDER CANCELLED";
            text = "We are so sorry to inform you that your order has been cancelled. Reach out us at +994557367002 if you think this is a misunderstanding.";
        } else {
            throw new UnexpectedOrderStatus(event);
        }
        this.sendMail(subject, text);
        log.info("Sent mail for {}", event);

    }
}
