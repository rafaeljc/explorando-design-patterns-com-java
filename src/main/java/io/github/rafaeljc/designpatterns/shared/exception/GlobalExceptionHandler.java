package io.github.rafaeljc.designpatterns.shared.exception;

import io.github.rafaeljc.designpatterns.notification.exception.InvalidNotificationException;
import io.github.rafaeljc.designpatterns.notification.exception.NotificationNotFoundException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(InvalidNotificationException.class)
	ProblemDetail handleInvalidNotification(InvalidNotificationException exception) {
		return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ProblemDetail handleInvalidBody(MethodArgumentNotValidException exception) {
		String reason = exception.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.sorted()
				.collect(Collectors.joining("; "));
		return problem(HttpStatus.BAD_REQUEST, reason);
	}

	// Cobre JSON malformado e valores fora do enum (ex.: canal "FAX")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ProblemDetail handleUnreadableBody(HttpMessageNotReadableException exception) {
		return problem(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou com valor não reconhecido");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	ProblemDetail handleInvalidPathVariable(MethodArgumentTypeMismatchException exception) {
		return problem(HttpStatus.BAD_REQUEST, "O parâmetro '" + exception.getName() + "' é inválido");
	}

	@ExceptionHandler(NotificationNotFoundException.class)
	ProblemDetail handleNotFound(NotificationNotFoundException exception) {
		return problem(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	// Rede de segurança: qualquer erro não previsto vira 500, sem expor detalhes ao cliente
	@ExceptionHandler(Exception.class)
	ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
		// Erros padrão do Spring (404, 405...) já trazem status e corpo próprios
		if (exception instanceof ErrorResponse errorResponse) {
			return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse.getBody());
		}
		log.error("event=request.failed status=500", exception);
		ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado");
		return ResponseEntity.internalServerError().body(body);
	}

	private ProblemDetail problem(HttpStatus status, String reason) {
		log.warn("event=request.rejected status={} reason=\"{}\"", status.value(), reason);
		return ProblemDetail.forStatusAndDetail(status, reason);
	}

}
