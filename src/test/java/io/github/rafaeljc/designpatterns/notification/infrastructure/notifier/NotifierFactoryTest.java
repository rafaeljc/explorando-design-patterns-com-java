package io.github.rafaeljc.designpatterns.notification.infrastructure.notifier;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.rafaeljc.designpatterns.notification.model.Channel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("NotifierFactory")
class NotifierFactoryTest {

	private final NotifierFactory factory = new NotifierFactory();

	@ParameterizedTest
	@EnumSource(Channel.class)
	@DisplayName("notifierFor devolve o notifier que atende o canal pedido")
	void notifierFor_eachChannel_returnsNotifierOfSameChannel(Channel channel) {
		assertThat(factory.notifierFor(channel).channel()).isEqualTo(channel);
	}

}
