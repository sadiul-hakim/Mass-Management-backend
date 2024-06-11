package org.massmanagement.util;

import org.massmanagement.service.AlertService;

public class BeanHandler {
    public static final AlertService alertService = SpringContext.getBean(AlertService.class);
}
