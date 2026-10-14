package io.github.rafaeljc.designpatterns.notification.model;

import io.github.rafaeljc.designpatterns.notification.exception.InvalidNotificationException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Notification {

	@Id
	@UuidGenerator(style = UuidGenerator.Style.VERSION_7)
	private UUID id;

	@Enumerated(EnumType.STRING)
	private Channel channel;

	private String recipient;
	private String subject;
	private String message;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	private Instant createdAt;

	// Exigido pelo JPA; não faz parte da API pública
	protected Notification() {
	}

	// Único caminho de criação: o objeto só nasce pronto, via Builder
	private Notification(Builder builder) {
		this.channel = builder.channel;
		this.recipient = builder.recipient;
		this.subject = builder.subject;
		this.message = builder.message;
		this.priority = builder.priority;
		this.createdAt = Instant.now();
	}

	// Os campos obrigatórios entram aqui, então não existe Builder incompleto
	public static Builder builder(Channel channel, String recipient, String message) {
		return new Builder(channel, recipient, message);
	}

	public UUID getId() {
		return id;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public Priority getPriority() {
		return priority;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public static class Builder {

		private final Channel channel;
		private final String recipient;
		private final String message;
		private String subject;
		private Priority priority = Priority.NORMAL; // valor padrão

		private Builder(Channel channel, String recipient, String message) {
			if (channel == null) {
				throw new InvalidNotificationException("O canal é obrigatório");
			}
			if (isBlank(recipient)) {
				throw new InvalidNotificationException("O destinatário é obrigatório");
			}
			if (isBlank(message)) {
				throw new InvalidNotificationException("A mensagem é obrigatória");
			}
			this.channel = channel;
			this.recipient = recipient;
			this.message = message;
		}

		private static boolean isBlank(String value) {
			return value == null || value.isBlank();
		}

		public Builder withSubject(String subject) {
			this.subject = subject;
			return this;
		}

		public Builder withPriority(Priority priority) {
			if (priority == null) {
				throw new InvalidNotificationException("A prioridade não pode ser nula");
			}
			this.priority = priority;
			return this;
		}

		public Notification build() {
			return new Notification(this);
		}

	}

}
