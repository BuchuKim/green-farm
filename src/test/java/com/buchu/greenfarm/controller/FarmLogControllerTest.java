//package com.buchu.greenfarm.controller;
//
//import com.buchu.greenfarm.code.FarmLogStatusCode;
//import com.buchu.greenfarm.dto.farmLog.CreateFarmLog;
//import com.buchu.greenfarm.dto.farmLog.FarmLogDetailDto;
//import com.buchu.greenfarm.service.FarmLogService;
//import com.google.gson.Gson;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.refEq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//// 모든 controller Component 가져오지 않고 특정 Controller 관련
//// (여기선 FarmLogController) test 수해하겠다는 annotation
//@WebMvcTest(FarmLogController.class)
//public class FarmLogControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    // spring context 에 bean 올리는 annotation
//    @MockBean // 가짜 객체이므로 행위 X -> Mockito given() 필요
//    FarmLogService farmLogService;
//
//    private Gson gson = new Gson();
//
//    private final FarmLogDetailDto demoFarmLogDetailDto = FarmLogDetailDto.builder()
//            .logContent("demo content of farm log detail dto")
//            .likeNum(0)
//            .commentNum(0)
//            .build();
//
//    @Test
//    void getFarmLogTest() throws Exception {
//        // given
//        given(farmLogService.getFarmLogDetail(any(Long.class)))
//                .willReturn(demoFarmLogDetailDto);
//
//        // when-then
//        mockMvc.perform(get("/farm-log/"+any(Long.class)))
//                .andExpect(jsonPath("$.logContent")
//                        .value("demo content of farm log detail dto"))
//                .andExpect(jsonPath("$.likeNum")
//                        .value(0))
//                .andExpect(jsonPath("$.commentNum")
//                        .value(0))
//                .andDo(print());
//
//        verify(farmLogService).getFarmLogDetail(any(Long.class));
//    }
//
//    @Test
//    void createFarmLogTest() throws Exception {
//
//        CreateFarmLog.Request request = CreateFarmLog.Request.builder()
//                .logContent("demo content of farm log").build();
//        CreateFarmLog.Response response = CreateFarmLog.Response.builder()
//                .logContent("demo content of farm log")
//                .farmLogStatusCode(FarmLogStatusCode.PRESENTED)
//                .build();
//
//        // given
//        given(farmLogService.createFarmLog(any(CreateFarmLog.Request.class)))
//                .willReturn(response);
//
//        String requestJson = gson.toJson(request);
//
//        // when, then
//        mockMvc.perform(
//                post("/farm-log")
//                        .content(requestJson)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.logContent").value("demo content of farm log"))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        // verify : 해당하는 동작이 테스트 과정 중 실행되었는지 확인
//        // refEq : CreateFarmLog.Request equality 비교가 "동일성"을 통해 이뤄지고 있기 때문에 (equals() 구현 X)
//        // expected argument 다른 오류 발생 --> refEq, 즉 동등성으로 비교하는 method 이용
//        verify(farmLogService).createFarmLog(refEq(request));
//    }
//}
