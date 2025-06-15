package match.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String confirmUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("會員註冊確認信");
        message.setText("請點選以下連結進行確認：\n" + confirmUrl);
        message.setFrom("kristoff0109@gmail.com");
        mailSender.send(message);
    }
}
