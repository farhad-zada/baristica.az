package com.shop.payment.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.shop.payment.dto.UserBalanceResponseDto;
import com.shop.payment.service.UserBalanceService;

@WebMvcTest(UserBalanceController.class)
@ActiveProfiles("test")
public class UserBalanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserBalanceService service;

    @Test
    void shouldWork() throws Exception {
        assert service != null;
    }

    @Test
    void getBalanceOf_ShouldReturnUserBalanceDto() throws Exception {
        Integer userId = 2000;
        UserBalanceResponseDto dto = new UserBalanceResponseDto(userId, 10000);
        when(this.service.getBalanceOf(userId)).thenReturn(dto);

        mockMvc.perform(get("/balance/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(dto.getUserId()))
                .andExpect(jsonPath("$.balance").value(dto.getBalance()));
        verify(service, times(1)).getBalanceOf(userId);
    }

    @Test
    void getBalanceOf_ShouldThrowWhenCalledWithInvalidUserId() throws Exception {
        Integer userId = -1;
        mockMvc.perform(get("/balance/{userId}", userId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).getBalanceOf(userId);
    }
}
