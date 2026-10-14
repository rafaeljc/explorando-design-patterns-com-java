package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import org.springframework.stereotype.Component;

@Component
public class NotifierFactory {

	// Factory Method: o chamador pede por canal e nunca conhece a classe concreta.
	// O switch sobre enum é exaustivo: um canal novo sem Notifier não compila.
	public Notifier notifierFor(Channel channel) {
		return switch (channel) {
			case EMAIL -> new EmailNotifier();
			case SMS -> new SmsNotifier();
			case PUSH -> new PushNotifier();
		};
	}

}
