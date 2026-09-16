package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.entraide.siipe.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendAccountCreationEmail(User user, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("supportit@entraide.ma", "Entraide Nationale SIIPE");
            helper.setTo(user.getEmail());
            helper.setSubject("Activation de votre compte SIIPE");

            Context context = new Context();
            context.setVariable("nom", user.getFullName());
            context.setVariable("email", user.getEmail());
            context.setVariable("resetLink", frontendUrl + "/auth/reset-password?token=" + resetToken);

            String htmlContent = templateEngine.process("email/account-creation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Account creation email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send account creation email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("supportit@entraide.ma", "Entraide Nationale SIIPE");
            helper.setTo(user.getEmail());
            helper.setSubject("Réinitialisation de votre mot de passe SIIPE");

            Context context = new Context();
            context.setVariable("nom", user.getFullName());
            context.setVariable("resetLink", frontendUrl + "/auth/reset-password?token=" + resetToken);

            String htmlContent = templateEngine.process("email/password-reset", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", user.getEmail(), e.getMessage());
        }
    }
}
