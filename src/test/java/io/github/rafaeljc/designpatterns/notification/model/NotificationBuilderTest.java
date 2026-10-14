package io.github.rafaeljc.designpatterns.notification.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.rafaeljc.designpatterns.notification.exception.InvalidNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Notification.Builder")
class NotificationBuilderTest {

	@Test
	@DisplayName("build com campos obrigatórios aplica os valores padrão")
	void build_requiredFieldsOnly_appliesDefaults() {
		Notification notification = Notification.builder(Channel.PUSH, "device-token", "Hello").build();

		assertThat(notification.getChannel()).isEqualTo(Channel.PUSH);
		assertThat(notification.getRecipient()).isEqualTo("device-token");
		assertThat(notification.getMessage()).isEqualTo("Hello");
		assertThat(notification.getSubject()).isNull();
		assertThat(notification.getPriority()).isEqualTo(Priority.NORMAL);
		assertThat(notification.getCreatedAt()).isNotNull();
	}

	@Test
	@DisplayName("build com campos opcionais preserva os valores informados")
	void build_optionalFields_keepsValues() {
		Notification notification = Notification.builder(Channel.EMAIL, "ana@mail.com", "Hello")
				.withSubject("Welcome")
				.withPriority(Priority.HIGH)
				.build();

		assertThat(notification.getSubject()).isEqualTo("Welcome");
		assertThat(notification.getPriority()).isEqualTo(Priority.HIGH);
	}

	@Test
	@DisplayName("builder sem canal lança InvalidNotificationException")
	void builder_nullChannel_throws() {
		assertThatThrownBy(() -> Notification.builder(null, "x", "Hello"))
				.isInstanceOf(InvalidNotificationException.class)
				.hasMessage("O canal é obrigatório");
	}

	@Test
	@DisplayName("builder com destinatário em branco lança InvalidNotificationException")
	void builder_blankRecipient_throws() {
		assertThatThrownBy(() -> Notification.builder(Channel.SMS, " ", "Hello"))
				.isInstanceOf(InvalidNotificationException.class)
				.hasMessage("O destinatário é obrigatório");
	}

	@Test
	@DisplayName("builder com mensagem ausente lança InvalidNotificationException")
	void builder_nullMessage_throws() {
		assertThatThrownBy(() -> Notification.builder(Channel.SMS, "x", null))
				.isInstanceOf(InvalidNotificationException.class)
				.hasMessage("A mensagem é obrigatória");
	}

	@Test
	@DisplayName("withPriority nulo lança InvalidNotificationException")
	void withPriority_null_throws() {
		Notification.Builder builder = Notification.builder(Channel.SMS, "x", "Hello");

		assertThatThrownBy(() -> builder.withPriority(null))
				.isInstanceOf(InvalidNotificationException.class)
				.hasMessage("A prioridade não pode ser nula");
	}

}
