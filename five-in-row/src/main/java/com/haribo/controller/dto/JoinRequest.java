package com.haribo.controller.dto;

import com.haribo.model.Player;
import lombok.Data;

@Data
public class JoinRequest {

    private Player player;
    private String gameId;
}
