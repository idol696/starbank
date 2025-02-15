package com.skypro.starbank.service;

import com.skypro.starbank.dto.ServiceInfoDTO;

public interface ManagementService {
    void clearCaches();
    ServiceInfoDTO getServiceInfo();
}
