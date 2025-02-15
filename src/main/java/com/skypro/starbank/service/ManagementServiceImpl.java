package com.skypro.starbank.service;

import com.skypro.starbank.dto.ServiceInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Service
@ConditionalOnProperty(prefix = "info", name = "build.name", matchIfMissing = true)
public class ManagementServiceImpl implements ManagementService {

    private static final Logger logger = LoggerFactory.getLogger(ManagementServiceImpl.class);
    private final BuildProperties buildProperties;

    public ManagementServiceImpl(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }


    @Override
    public ServiceInfoDTO getServiceInfo() {
        return new ServiceInfoDTO(buildProperties.getName(), buildProperties.getVersion());
    }

    /**
     * Очищает все кэши и обновляет базу данных.
     */
    @Caching(evict = {@CacheEvict(value = "rulesCheck", allEntries = true)})
    public void clearCaches() {
        logger.info("♻ Очистка кеша выполнена.");
    }
}
