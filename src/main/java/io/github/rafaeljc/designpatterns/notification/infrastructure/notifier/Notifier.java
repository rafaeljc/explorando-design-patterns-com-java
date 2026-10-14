package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Notification;

public interface Notifier {

	Channel channel();

	void send(Notification notification);

}
