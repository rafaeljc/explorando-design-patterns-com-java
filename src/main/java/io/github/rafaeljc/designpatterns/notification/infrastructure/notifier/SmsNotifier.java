package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import io.github.rafaeljc.designpatterns.notification.exception.InvalidNotificationException;
import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Notification;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsNotifier implements Notifier {

	private static final Logger log = LoggerFactory.getLogger(SmsNotifier.class);
	private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,13}$");
	private static final int MAX_SMS_LENGTH = 160;

	@Override
	public Channel channel() {
		return Channel.SMS;
	}

	@Override
	public void send(Notification notification) {
		if (!PHONE_PATTERN.matcher(notification.getRecipient()).matches()) {
			throw new InvalidNotificationException("O destinatário não é um telefone válido (10 a 13 dígitos)");
		}
		if (notification.getMessage().length() > MAX_SMS_LENGTH) {
			throw new InvalidNotificationException("A mensagem de SMS excede " + MAX_SMS_LENGTH + " caracteres");
		}
		log.info("event=notification.sent channel={} priority={}", channel(), notification.getPriority());
	}

}
