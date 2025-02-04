package com.skypro.starbank.service;

import java.util.Map;

public interface ManagementService {
    void clearCaches();
    Map<String, String> getServiceInfo();
}
