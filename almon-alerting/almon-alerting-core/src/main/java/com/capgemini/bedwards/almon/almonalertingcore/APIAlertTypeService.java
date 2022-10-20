package com.capgemini.bedwards.almon.almonalertingcore;

import com.capgemini.bedwards.almon.almondatastore.models.alerts.APIAlertType;

public interface APIAlertTypeService {

    APIAlertType saveAlertType(String name, String description, String url, int httpStatus);
}
