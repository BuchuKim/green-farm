package com.buchu.greenfarm;

import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.repository.FarmLogRepository;
import com.buchu.greenfarm.repository.NotificationRepository;
import com.buchu.greenfarm.repository.UserRepository;
import com.buchu.greenfarm.service.NotificationService;
import com.buchu.greenfarm.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GreenFarmApplicationTests {
	@Autowired
	NotificationService notificationService;
	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	FarmLogRepository farmLogRepository;

	@BeforeEach
	public void setDB() {
		userRepository.save(User.builder()
				.userId("demoId1")
				.email("demoEmail1@email.com")
				.name("demo1")
				.build());

		userRepository.save(User.builder()
				.userId("demoId2")
				.email("demoEmail2@email.com")
				.name("demo2")
				.build());
	}

	@Test
	@DisplayName("태그 알림")
	@Transactional
	public void tagNotificationTest() {
		User demo1 = userRepository.findByUserId("demoId1").orElseThrow();
		User demo2 = userRepository.findByUserId("demoId2").orElseThrow();

		String userInput = "demoId1이 @demoId2 그리고 @sh814 에게 알림을 보내려고 한다.";
		FarmLog savedFarmLog = farmLogRepository.save(FarmLog.builder().logContent(userInput).author(demo1).build());

		notificationService.sendTagNotification(notificationService.searchTagList(userInput),
				demo2, savedFarmLog);

		Assertions.assertThat(notificationRepository.findByReceivingUser(
				userRepository.findByUserId("demoId2").orElseThrow())).isNotEmpty();

		for (Notification notification : notificationRepository.findAll()) {
			System.out.println(notification.getMessage());
		}
	}

	@Test
	@DisplayName("팔로우 알림")
	@Transactional
	public void followNotificationTest() {
		userService.follow("demoId1", "demoId2");

		Assertions.assertThat(notificationRepository.findByReceivingUser(
				userRepository.findByUserId("demoId2").orElseThrow()
		)).isNotEmpty();
	}

}
