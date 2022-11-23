package org.bert.carehelper.service;

import org.bert.carehelper.entity.CommandResponse;

public interface Service {

    CommandResponse doCommand(String content);
}
