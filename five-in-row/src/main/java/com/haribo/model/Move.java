package com.haribo.model;

import lombok.Data;

@Data
public class Move {

    private Token token;
    private Integer coorX;
    private Integer coorY;
    private String gameId;
}
