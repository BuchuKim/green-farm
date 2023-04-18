package com.buchu.greenfarm;

import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.entity.User;
import com.buchu.greenfarm.repository.NotificationRepository;
import com.buchu.greenfarm.repository.UserRepository;
import com.buchu.greenfarm.service.NotificationService;
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
	@DisplayName("알림이 잘 갔는지 확인")
	@Transactional
	public void notificationTest() {
		String userInput = "demoId1이 @demoId2 그리고 @sh814 에게 알림을 보내려고 한다.";

		notificationService.sendTagAlarm(notificationService.searchTagList(userInput),
				userRepository.findByUserId("demoId1").orElseThrow());

		Assertions.assertThat(notificationRepository.findByReceivingUser(
				userRepository.findByUserId("demoId2").orElseThrow())).isNotEmpty();

		for (Notification notification : notificationRepository.findAll()) {
			System.out.println(notification.getMessage());
		}
	}

}
