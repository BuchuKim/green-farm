package com.buchu.greenfarm.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class NotificationDtoServiceTest {
    @InjectMocks
    NotificationService notificationService;

    @Test
    @DisplayName("tag id를 잘 찾는지 확인")
    public void findTag() {
        // given
        String userInput = "이 input에는 @sh814 님과 @qkqdlek32 님의 태그가 추가되어 있습니다.";

        // when
        List<String> taggedIds = notificationService.searchTagList(userInput);

        // then
        Assertions.assertThat(taggedIds).contains("sh814","qkqdlek32");
        Assertions.assertThat(taggedIds.contains("말도 안되는 id")).isFalse();
    }
}
