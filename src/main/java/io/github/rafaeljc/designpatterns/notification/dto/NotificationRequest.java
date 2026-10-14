package io.github.rafaeljc.designpatterns.notification.dto;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import io.github.rafaeljc.designpatterns.notification.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
		@NotNull(message = "O canal é obrigatório") Channel channel,
		@NotBlank(message = "O destinatário é obrigatório")
		@Size(max = 255, message = "O destinatário deve ter no máximo 255 caracteres") String recipient,
		@NotBlank(message = "A mensagem é obrigatória")
		@Size(max = 1000, message = "A mensagem deve ter no máximo 1000 caracteres") String message,
		@Size(max = 255, message = "O assunto deve ter no máximo 255 caracteres") String subject,
		Priority priority) {
}
