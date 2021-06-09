package com.haribo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Token {
    X( 1), O(2);

    private Integer value;
}
