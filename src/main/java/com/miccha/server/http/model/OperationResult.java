package com.miccha.server.http.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OperationResult {
    private int errorCode;
}
