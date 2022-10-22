package com.capgemini.bedwards.almon.almonalertingcore;

import com.capgemini.bedwards.almon.almondatastore.models.alert.APIAlertType;
import com.capgemini.bedwards.almon.almondatastore.repository.AlertTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AlertTypeServiceImpl implements APIAlertTypeService {


    @Autowired
    private AlertTypeRepository alertTypeRepository;

    @Override
    public APIAlertType saveAlertType(String name, String description, String url, int expectedStatus) {


        APIAlertType apiAlertType = APIAlertType.builder()
                .name(name)
                .description(description)
                .url(url)
                .expectedStatus(expectedStatus)
                .build();
        log.debug("Alert Type received: " + apiAlertType);
        return alertTypeRepository.save(apiAlertType);
    }
}
