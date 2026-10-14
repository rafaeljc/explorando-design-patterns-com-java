package io.github.rafaeljc.designpatterns.notification.controller;

import io.github.rafaeljc.designpatterns.notification.dto.NotificationRequest;
import io.github.rafaeljc.designpatterns.notification.dto.NotificationResponse;
import io.github.rafaeljc.designpatterns.notification.service.NotificationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

	private final NotificationService service;

	public NotificationController(NotificationService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<NotificationResponse> send(@Valid @RequestBody NotificationRequest request) {
		NotificationResponse response = NotificationResponse.from(service.send(request));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(response.id())
				.toUri();
		return ResponseEntity.created(location).body(response);
	}

	@GetMapping
	public List<NotificationResponse> findAll() {
		return service.findAll().stream().map(NotificationResponse::from).toList();
	}

	@GetMapping("/{id}")
	public NotificationResponse findById(@PathVariable UUID id) {
		return NotificationResponse.from(service.findById(id));
	}

}
