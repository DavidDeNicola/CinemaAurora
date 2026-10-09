package org.elis.movieexplorer.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
	
}
