package com.skypro.starbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.starbank.dto.ServiceInfoDTO;
import com.skypro.starbank.service.ManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ManagementController.class)
@ActiveProfiles("test")
class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManagementServiceImpl managementService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldClearCachesSuccessfully() throws Exception {
        doNothing().when(managementService).clearCaches();

        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isOk());

        verify(managementService, times(1)).clearCaches();
    }

    @Test
    void shouldGetServiceInfoSuccessfully() throws Exception {
        ServiceInfoDTO serviceInfo = new ServiceInfoDTO("Application", "0.1");
        when(managementService.getServiceInfo()).thenReturn(serviceInfo);

        mockMvc.perform(get("/management/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Application"))
                .andExpect(jsonPath("$.version").value("0.1"));

        verify(managementService, times(1)).getServiceInfo();
    }
}
