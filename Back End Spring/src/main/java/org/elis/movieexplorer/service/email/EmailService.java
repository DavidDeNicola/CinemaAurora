package org.elis.movieexplorer.service.email;

import java.util.AbstractMap.SimpleEntry;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender sender;
	
	public void simpleMail(String mittente, String destinatario, String corpo) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom(mittente);
		mail.setTo(destinatario);
		mail.setText(corpo);
		sender.send(mail);
	}
	
	public void emailConAllegato(String mittente, String destinatario, String corpo, String path){
		MimeMessage mail = sender.createMimeMessage();

		sender.send(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			helper.setFrom(mittente);
			helper.setTo(destinatario);
			helper.setText(corpo);
			FileSystemResource file = new FileSystemResource(path);
			helper.addAttachment(file.getFilename(), file);
		});
	}
	
}
