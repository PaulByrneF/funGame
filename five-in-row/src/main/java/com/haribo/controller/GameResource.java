package com.haribo.controller;

import com.haribo.controller.dto.JoinRequest;
import com.haribo.exception.InvalidGameException;
import com.haribo.exception.InvalidMoveException;
import com.haribo.exception.InvalidParamException;
import com.haribo.exception.NotFoundException;
import com.haribo.model.Game;
import com.haribo.model.Move;
import com.haribo.model.Player;
import com.haribo.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameResource {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameState(@PathVariable String gameId) throws InvalidParamException {
        log.info("retrieve game by gameId: {}", gameId);
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @PostMapping("/join")
    public ResponseEntity<Game> joinGame(@RequestBody JoinRequest request) throws InvalidParamException, InvalidGameException {
        log.info("start game request: {}", request);
        return ResponseEntity.ok(gameService.joinGameServer(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/join/random")
    public ResponseEntity<Game> joinRandomGame(@RequestBody Player player) throws NotFoundException {
        log.info("join random game: {}", player);
        return ResponseEntity.ok(gameService.joinRandomGame(player));
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestBody Move request) throws InvalidGameException, NotFoundException, InvalidMoveException {
        log.info("make move: {}", request);
        Game game = gameService.makeMove(request);
        return ResponseEntity.ok(game);
    }

}
