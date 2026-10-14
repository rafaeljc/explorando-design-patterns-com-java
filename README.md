# Notification API

API REST de notificações com Spring Boot 4 e Java 25, criada para o desafio
[Explorando Padrões de Projetos na Prática com Java](DESAFIO.md) da DIO.

O foco são dois padrões de projeto, aplicados em um domínio pequeno de propósito:

| Padrão | Onde está |
|---|---|
| **Factory Method** | [`NotifierFactory`](src/main/java/io/github/rafaeljc/designpatterns/notification/infrastructure/notifier/NotifierFactory.java) |
| **Builder** | [`Notification.Builder`](src/main/java/io/github/rafaeljc/designpatterns/notification/model/Notification.java) |

## Stack

Java 25 · Spring Boot 4 · Spring Data JPA · H2 (em memória) · Bean Validation · springdoc-openapi · Maven

## Como rodar

Pré-requisito: JDK 25.

```bash
./mvnw spring-boot:run
```

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Console do H2 | http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:notifications`, usuário `sa`, senha vazia) |

Testes:

```bash
./mvnw test
```

## Estrutura

O projeto é organizado por funcionalidade (_vertical slice_), com as camadas do Spring dentro dela:

```
notification/
├── controller/       NotificationController
├── dto/              NotificationRequest, NotificationResponse
├── exception/        InvalidNotificationException, NotificationNotFoundException
├── infrastructure/
│   └── notifier/     Notifier, NotifierFactory, EmailNotifier, SmsNotifier, PushNotifier
├── model/            Notification (+ Builder), Channel, Priority
├── repository/       NotificationRepository
└── service/          NotificationService, NotificationServiceImpl
    └── usecase/      SendNotificationUseCase, ListNotificationsUseCase, GetNotificationUseCase
shared/
└── exception/        GlobalExceptionHandler
```

Os identificadores são UUID v7, ordenáveis por data de criação.

## Endpoints

Base: `/api/v1/notifications`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/` | Envia uma notificação (`201` com o header `Location`) |
| `GET` | `/` | Lista as notificações |
| `GET` | `/{id}` | Busca uma notificação (`404` se não existir) |

Os erros seguem o formato `ProblemDetail` (RFC 9457).

### Regras por canal

| Canal | Destinatário | Regras adicionais |
|---|---|---|
| `EMAIL` | e-mail válido | `subject` obrigatório |
| `SMS` | telefone com 10 a 13 dígitos | mensagem com até 160 caracteres |
| `PUSH` | token do dispositivo | — |

### Exemplos

E-mail válido (`201`):

```bash
curl -X POST localhost:8080/api/v1/notifications \
  -H "Content-Type: application/json" \
  -d '{"channel":"EMAIL","recipient":"ana@mail.com","subject":"Bem-vinda","message":"Olá!","priority":"HIGH"}'
```

E-mail sem assunto (`400`):

```bash
curl -X POST localhost:8080/api/v1/notifications \
  -H "Content-Type: application/json" \
  -d '{"channel":"EMAIL","recipient":"ana@mail.com","message":"Olá!"}'
```

```json
{"detail":"O assunto é obrigatório para e-mail","instance":"/api/v1/notifications","status":400,"title":"Bad Request"}
```

`priority` é opcional (`LOW`, `NORMAL`, `HIGH`) e o padrão é `NORMAL`.

## Factory Method

O `Notifier` é a interface comum. Cada canal tem a sua implementação (`EmailNotifier`, `SmsNotifier`, `PushNotifier`), com as próprias regras de validação e de envio.

O código que envia a notificação não conhece nenhuma classe concreta. Ele só pede à factory o notifier do canal:

```java
notifierFactory.notifierFor(notification.getChannel()).send(notification);
```

```java
public Notifier notifierFor(Channel channel) {
	return switch (channel) {
		case EMAIL -> new EmailNotifier();
		case SMS -> new SmsNotifier();
		case PUSH -> new PushNotifier();
	};
}
```

**Por que `switch` e não um mapa de beans?** O `switch` sobre `enum` é exaustivo: se um novo canal for adicionado ao `Channel` e esquecido na factory, o código não compila. Para um conjunto fixo e pequeno de canais, isso é mais simples e deixa o padrão explícito. Um registro de beans injetados (`Map<Channel, Notifier>`) só compensa quando os canais são abertos, como plugins.

**Observação sobre o GoF:** na definição original, o Factory Method delega a criação a subclasses. A forma usada aqui, com um método que escolhe a implementação concreta, é uma variação mais enxuta e comum na prática (às vezes chamada de Simple Factory). O objetivo é o mesmo: isolar quem cria de quem usa.

## Builder

`Notification` não tem construtor público nem setters. A única forma de criá-la é pelo `Notification.Builder`:

```java
Notification notification = Notification.builder(Channel.EMAIL, "ana@mail.com", "Olá!")
		.withSubject("Bem-vinda")
		.withPriority(Priority.HIGH)
		.build();
```

- **Campos obrigatórios** (`channel`, `recipient`, `message`) entram no construtor do Builder e são validados na hora. Não existe Builder incompleto.
- **Campos opcionais** (`subject`, `priority`) usam métodos `withXxx`. A prioridade tem o valor padrão `NORMAL`.
- O objeto criado nunca está inválido.

A validação do Builder é uma guarda de domínio. Na borda da API, o `NotificationRequest` usa Bean Validation para devolver erros amigáveis, todos de uma vez.

### Por que Lombok não foi utilizado

O Lombok geraria o Builder com um `@Builder`, e o código ficaria menor. Aqui ele foi evitado de propósito: o objetivo do desafio é **mostrar o padrão**, e a anotação esconde exatamente o que se quer aprender (a classe aninhada, o construtor privado, os métodos fluentes e a validação). Em um projeto de produção, usar o Lombok é uma escolha razoável.

## Logs

Os logs são simples e legíveis, no formato `chave=valor`. O destinatário nunca é registrado, pois pode ser um dado pessoal.

```
10:42:13.512 INFO  EmailNotifier - event=notification.sent channel=EMAIL priority=HIGH subject="Bem-vinda"
10:42:13.530 INFO  SendNotificationUseCase - event=notification.saved id=01a13a5e-eaf5-76f4-b326-9dd100638766 channel=EMAIL
10:42:20.004 WARN  GlobalExceptionHandler - event=request.rejected status=400 reason="A mensagem de SMS excede 160 caracteres"
```

## Testes

Os testes são mínimos de propósito. O escopo do projeto é demonstrar os dois padrões, então a cobertura se concentra neles:

- `NotificationBuilderTest`: valores padrão, campos opcionais e validação dos obrigatórios.
- `NotifierFactoryTest`: cada canal resolve o notifier correspondente.
- `DesignpatternsApplicationTests`: o contexto do Spring sobe com tudo conectado.

Controller, casos de uso, notifiers e o tratamento de erros não têm testes automatizados próprios. Em um projeto de produção, essas camadas seriam cobertas (por exemplo, com `@WebMvcTest` e testes de integração). Aqui, o comportamento da API foi verificado manualmente, pelo Swagger UI e por `curl`.
