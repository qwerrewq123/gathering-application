package spring.myproject.service.fcm;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.dto.request.fcm.TopicNotificationRequestDto;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.fcm.FCMTokenTopic;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.fcm.UserTopic;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.fcm.AlreadySubscribeTopicException;
import spring.myproject.common.exception.fcm.NotFoundTopicException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.fcm.FCMTokenRepository;
import spring.myproject.repository.fcm.FCMTokenTopicRepository;
import spring.myproject.repository.fcm.TopicRepository;
import spring.myproject.repository.fcm.UserTopicRepository;
import spring.myproject.repository.notification.NotificationRepository;
import spring.myproject.repository.user.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static spring.myproject.dto.request.user.UserRequestDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

	private final TopicRepository topicRepository;
	private final FCMTokenRepository fcmTokenRepository;
	private final FCMTokenTopicRepository fcmTokenTopicRepository;
	private final UserTopicRepository userTopicRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	final int TOKEN_EXPIRATION_MONTHS = 2;

	public void sendByTopic(TopicNotificationRequestDto topicNotificationRequestDto,Topic topic) {

		Message message = Message.builder()
			.setTopic(topicNotificationRequestDto.getTopic())
			.setNotification(Notification.builder()
				.setTitle(topicNotificationRequestDto.getTitle())
				.setBody(topicNotificationRequestDto.getContent())
				.setImage(topicNotificationRequestDto.getImg())
				.build())
			.putData("click_action", topicNotificationRequestDto.getUrl())
			.build();

		try {
			FirebaseMessaging.getInstance().send(message);
			notificationRepository.save(spring.myproject.entity.notification.Notification.builder()
					.topic(topic)
					.img(topicNotificationRequestDto.getImg())
					.url(topicNotificationRequestDto.getUrl())
					.title(topicNotificationRequestDto.getTitle())
					.content(topicNotificationRequestDto.getContent())
					.build());

		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}

	}

	public void subscribeToTopic(String topicName, List<String> tokens) {

		List<String> failedTokens = new ArrayList<>();
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopicAsync(tokens, topicName).get();
			if (response.getFailureCount() > 0) {
				response.getErrors().forEach(error -> {
					failedTokens.add(tokens.get(error.getIndex()));
				});
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException();
		}
		if (!failedTokens.isEmpty()) {
			fcmTokenTopicRepository.deleteByTokenValueIn(failedTokens);
			fcmTokenRepository.deleteByTokenValueIn(failedTokens);
		}
	}

	@Transactional
	public void subscribeToTopics(String topicName, Long userId) {

		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new NotFoundTopicException("not found topic"));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundUserException("not found user"));
		if (userTopicRepository.existsByTopicAndUser(topicName, user.getId())) {
			throw new AlreadySubscribeTopicException("already subscribe topic");
		}
		List<FCMToken> userTokens = user.getTokens();
		UserTopic topicMember = UserTopic.builder()
				.topic(topic)
				.user(user)
				.build();
		userTopicRepository.save(topicMember);
		List<FCMTokenTopic> fcmTokenTopics = userTokens.stream()
				.map(token -> new FCMTokenTopic(topic, token))
				.collect(Collectors.toList());
		fcmTokenTopicRepository.saveAll(fcmTokenTopics);
		List<String> tokenValues = userTokens.stream()
				.map(fcmToken -> fcmToken.getTokenValue())
				.collect(Collectors.toList());
		subscribeToTopic(topicName, tokenValues);
	}

	public void unsubscribeFromTopic(String topic, List<String> tokens) {

		List<String> failedTokens = new ArrayList<>();
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(tokens, topic).get();
			if (response.getFailureCount() > 0) {
				response.getErrors().forEach(error -> {
					failedTokens.add(tokens.get(error.getIndex()));
				});
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException();
		}
		if (!failedTokens.isEmpty()) {
			fcmTokenRepository.deleteByTokenValueIn(failedTokens);
		}
	}

	@Transactional
	public void unsubscribeFromTopics(String topicName, Long userId) {

		Topic topic = topicRepository.findByTopicName(topicName)
				.orElseThrow(() -> new NotFoundTopicException("not found topic"));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundUserException("not found user"));
		userTopicRepository.deleteByTopicAndUser(topic, user);
		fcmTokenTopicRepository.deleteByTopic(topic);
		List<FCMToken> memberTokens = user.getTokens();
		List<String> tokenValues = memberTokens.stream()
				.map(fcmToken -> fcmToken.getTokenValue())
				.collect(Collectors.toList());
		unsubscribeFromTopic(topicName, tokenValues);
	}

	@Transactional
	public void saveFCMToken(SignInRequest signInRequest) {

		User user = userRepository.findByUsername(signInRequest.getUsername())
				.orElseThrow(() -> new NotFoundUserException("not found user"));
		Optional<FCMToken> existingToken = fcmTokenRepository.findByTokenValueAndUser(signInRequest.getFcmToken(), user.getId());
		if (existingToken.isPresent()) {
			FCMToken fcmToken = existingToken.get();
			fcmToken.changeExpirationDate(2);
		} else {
			FCMToken fcmToken = FCMToken.builder()
					.tokenValue(signInRequest.getFcmToken())
					.user(user)
					.expirationDate(LocalDate.now().plusMonths(TOKEN_EXPIRATION_MONTHS))
					.build();
			fcmTokenRepository.save(fcmToken);

			List<UserTopic> userTopics = userTopicRepository.findByUser(user);
			List<Topic> subscribedTopics = userTopics.stream()
					.map(UserTopic::getTopic)
					.distinct()
					.collect(Collectors.toList());

			List<FCMTokenTopic> newSubscriptions = subscribedTopics.stream()
					.map(topic -> new FCMTokenTopic(topic, fcmToken))
					.collect(Collectors.toList());
			fcmTokenTopicRepository.saveAll(newSubscriptions);

			for (Topic topic : subscribedTopics) {
				subscribeToTopic(topic.getTopicName(), Collections.singletonList(fcmToken.getTokenValue()));
			}
		}
	}

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void unsubscribeExpiredTokens() {
		LocalDate now = LocalDate.now();
		List<FCMToken> expiredTokens = fcmTokenRepository.findByExpirationDate(now);
		List<FCMTokenTopic> topicTokens = fcmTokenTopicRepository.findByFcmTokenIn(expiredTokens);
		List<String> tokenValues = expiredTokens.stream()
				.map(token -> token.getTokenValue())
				.collect(Collectors.toList());
		topicTokens.forEach(topicToken -> {
			unsubscribeFromTopic(topicToken.getTopic().getTopicName(), tokenValues);
		});
		fcmTokenTopicRepository.deleteAll(topicTokens);
		fcmTokenRepository.deleteAll(expiredTokens);
	}
}
