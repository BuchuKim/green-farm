//package com.buchu.greenfarm.service;
//
//import com.buchu.greenfarm.code.FarmLogStatusCode;
//import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
//import com.buchu.greenfarm.dto.farmLog.FarmLogDetailDto;
//import com.buchu.greenfarm.entity.FarmLog;
//import com.buchu.greenfarm.repository.FarmLogRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//
//
//// mockitoExtension 사용해서 test 진행하겠다는 표지
//@ExtendWith(MockitoExtension.class)
//public class FarmLogServiceTest {
//    // @ExtendWith 으로 @Mock 사용 가능해졌다
//    // @Mock : 빈 껍데기 객체
//    @Mock
//    private FarmLogRepository farmLogRepository;
//
//    // farmLogService 단위 테스트를 진행할 것이다.
//    // @InjectMocks: mock 으로 띄워진 객체들을 얘한테 주입
//    // @Mock 객체 farmLogRepository 가 farmLogService 객체에 주입되게 된다!
//    @InjectMocks
//    private FarmLogService farmLogService;
//
//    FarmLog farmLog = FarmLog.builder()
//            .logContent("log content of demo farm log")
//            .farmLogStatusCode(FarmLogStatusCode.PRESENTED)
//            .build();
//
//    @Test
//    void getFarmLogDetailTest() {
//        // given() 에 Mock object 행위 정의
//        given(farmLogRepository.findById(anyLong()))
//                .willReturn(Optional.of(farmLog));
//
//        FarmLogDetailDto farmLogDetailDto = farmLogService.getFarmLogDetail(10L);
//
//        assertEquals(FarmLogStatusCode.PRESENTED,farmLogDetailDto.getFarmLogStatusCode());
//        assertEquals("log content of demo farm log",farmLogDetailDto.getLogContent());
//
//        verify(farmLogRepository).findById(anyLong());
//    }
//
//    @Test
//    void createFarmLogTest() {
//        CreateFarmLog.Request request = CreateFarmLog.Request.builder()
//                .logContent(farmLog.getLogContent()).build();
//
//        given(farmLogRepository.save(any(FarmLog.class)))
//                .willReturn(farmLog);
//
//        CreateFarmLog.Response response = farmLogService.createFarmLog(request);
//
//        assertEquals(FarmLogStatusCode.PRESENTED, response.getFarmLogStatusCode());
//        assertEquals(farmLog.getLogContent(), response.getLogContent());
//
//        verify(farmLogRepository).save(any(FarmLog.class));
//    }
//
//}
