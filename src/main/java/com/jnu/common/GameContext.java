package com.jnu.common;

import com.jnu.view.Game;
import com.jnu.view.GameInformation;
import com.jnu.view.GameRound;
import com.jnu.view.GameUser;

/**
 * @author ：Killer
 * @date ：Created in 20-10-23 下午3:21
 * @description：游戏当前时刻当前用户的状态信息
 * @modified By：
 * @version: version
 */
public class GameContext {

    /**
     * 当前的游戏
     */
    private Game game;

    /**
     * 当前用户的游戏信息
     */
    private GameInformation gameInformation;

    /**
     * 当前的游戏回合
     */
    private GameRound gameRound;

    /**
     * 当前的游戏用户信息
     */
    private GameUser gameUser;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public void setGameInformation(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
    }

    public GameRound getGameRound() {
        return gameRound;
    }

    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public GameUser getGameUser() {
        return gameUser;
    }

    public void setGameUser(GameUser gameUser) {
        this.gameUser = gameUser;
    }
}
