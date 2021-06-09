package com.haribo.service;

import com.haribo.datastore.GameData;
import com.haribo.exception.InvalidGameException;
import com.haribo.exception.InvalidMoveException;
import com.haribo.exception.InvalidParamException;
import com.haribo.exception.NotFoundException;
import com.haribo.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.haribo.model.GameStatus.*;

@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(Player player){
        Game game = new Game();
        game.setBoard(new int[6][9]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(NEW);
        GameData.getInstance().setGame(game);
        return game;
    }

    public Game getGame(String gameId) throws InvalidParamException {
        if(!GameData.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParamException("Sorry, there is no such game found at :gameId: ");
        }

        return GameData.getInstance().getGames().get(gameId);
    }

    public Game joinGameServer(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if(!GameData.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParamException("Sorry, there is no such game found at :gameId: ");
        }

        Game game = GameData.getInstance().getGames().get(gameId);

        if(game.getPlayer2() != null) {
            throw new InvalidGameException("Sorry, Game Server is full");
        }

        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameData.getInstance().setGame(game);
        return game;
    }

    public Game joinRandomGame(Player player2) throws NotFoundException {
        Game game = GameData.getInstance().getGames().values().stream()
                .filter(it->it.getStatus().equals(NEW))
                .findFirst().orElseThrow(()-> new NotFoundException("Sorry the game servers are full at the moment"));
        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameData.getInstance().setGame(game);
        return game;
    }

    public Game makeMove(Move move) throws NotFoundException, InvalidGameException, InvalidMoveException {

        //Check for game based on gameId
        if (!GameData.getInstance().getGames().containsKey(move.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        //Check that game is in valid state
        Game game = GameData.getInstance().getGames().get(move.getGameId());
        if (game.getStatus().equals(CONCLUDED)) {
            throw new InvalidGameException("Game has already been concluded");
        }

        int[][] board = game.getBoard();

        //Check for full column
        if(columnIsFull(board, move.getCoorY())){
            throw new InvalidMoveException("Sorry, this column is full");
        }

        //Check if token is within bounds
        if(!(move.getCoorY() >= 0 && move.getCoorY() < board.length)){
            throw new InvalidMoveException("Sorry, this move is out of bounds. Please try again");
        }

        if((placeToken(board, move))) {
            if(checkWinner(game.getBoard(), move, Token.X)) {
                game.setWinner(Token.X);
            };
            GameData.getInstance().setGame(game);
        }


//        checkWinner(game.getBoard(), Token.O);


        return game;

    }

    private boolean checkWinner(int[][] board, Move move, Token token) {

        boolean winner = false;

        return (checkVertically(board, move)) ? true : checkHorizontally(board, move);
//        checkHorizontally();
//        checkDiagonally();

    }

    private void checkDiagonally() {

        /**
         * Board Layout: Left Top -> Bottom Right
         *
         *       0, 1, 2, 3, 4
         *    0 [*][*][*][*][*][5][6][7][8] <- move.getCoorY
         *    1 [*][*][*][*][*][*][ ][ ][ ]
         *      [2][*][*][*][*][*][*][ ][ ]
         *      [3][ ][*][*][*][*][*][*][ ]
         *      [4][ ][ ][*][*][*][*][*][*]
         *      [5][ ][ ][ ][*][*][*][*][*]
         *
         *
         *   Brainstorming Solution: Subtract X from Y = Loop({{Delta}})
         *   #1 move[x][y] = {2, 1} => calc(2-1) => 1   #Start Loop from coor[0,1] -> if positive place at x axis
         *   #2 move[x][y] = {4, 5} => calc(4-5) => -1   #Start Loop from coor[0,1] -> if negative place as y axis
         *   #3 move[x][y] = {4, 4} => calc(4-4) => 0   #start loop from coor[0, 0] -> if 0 start 0, 0
         */

    }

    private boolean checkHorizontally(int[][] board, Move move) {

        boolean win = false;
        System.out.println(move.getCoorX());
        System.out.println(board.length);
        System.out.println(move.getCoorX());
        System.out.println(move.getCoorY());
        System.out.println(move.getToken().getValue());

        int count = 0;

        for (int i = 0 ; i < board[0].length; i++) {
            System.out.println(i);

            if (board[move.getCoorX()][i] == move.getToken().getValue())
                count++;
            else
                count = 0;

            if (count >= 5)
                win = true;
        }
        return (win) ? true : false;
    }

    private boolean checkVertically(int[][] board, Move move) {

        boolean win = false;
        int count = 0;
        for (int i=0;i<board.length;i++)
        {
            if (board[i][move.getCoorY()]== move.getToken().getValue())
                count++;
            else
                count=0;

            if (count>=5)
                win = true;
        }
        return (win) ? true : false;

//        boolean win = false;
//        int countLinearTokens = 0;
//        for(int row = board.length-1; row >= 0; row--){
//            if(board[row][move.getCoorY()] == move.getToken().getValue()){
//                countLinearTokens++;
//                System.out.println(board[row][move.getCoorY()]);
//                System.out.println(move.getToken().getValue().toString());
//
//            } else {
//                countLinearTokens = 0;
//            }
//
//            if(countLinearTokens > 4 ) {
//                win = true;
//                break;
//            }
//        }
//        System.out.println(win);
//        System.out.println(countLinearTokens);
//        return (win) ? true : false;
    }

    /**
     * Board Layout
     * [0][1][2][3][4][5][6][7][8] <- move.getCoorY
     * [1][ ][ ][ ][ ][ ][ ][ ][ ]
     * [2][ ][ ][ ][ ][ ][ ][ ][ ]
     * [3][ ][ ][ ][ ][ ][ ][ ][ ]
     * [4][ ][ ][ ][ ][ ][ ][ ][ ]
     * [5][ ][ ][ ][ ][ ][ ][ ][ ]
     */

    private boolean placeToken(int[][] board, Move move) {

        boolean tokenPlaced = false;
        for ( int row = board.length - 1; row >= 0; row--) {
            if ( board[row][move.getCoorY()] == 0) {
                board[row][move.getCoorY()] = move.getToken().getValue();
                move.setCoorX(row);
                tokenPlaced = true;
                break;
            } else {
                continue;
            }
        }
        return tokenPlaced;
    }




        /**
         * Board Layout
         * [0][1][2][3][4][5][6][7][8]
         * [1][ ][ ][ ][ ][ ][ ][ ][ ]
         * [2][ ][ ][ ][ ][ ][ ][ ][ ]
         * [3][ ][ ][ ][ ][ ][ ][ ][ ]
         * [4][ ][ ][ ][ ][ ][ ][ ][ ]
         * [5][ ][ ][ ][ ][ ][ ][ ][ ]
         */

        public Boolean columnIsFull ( int[][] board, int col){
            return (!(board[0][col] == 0));
        }
    }

