package de.palmen_it.android.games.aconn4;

import de.palmen_it.games.p4j.gamelogic.Board;
import de.palmen_it.games.p4j.gamelogic.Player;

public class GameState {
	
	private Board _board;
	private Player _player1;
	private Player _player2;
	private Player _activePlayer;
	
	public Board getBoard() {
		return _board;
	}
	public void setBoard(Board board) {
		_board = board;
	}
	public Player getPlayer1() {
		return _player1;
	}
	public void setPlayer1(Player player1) {
		_player1 = player1;
	}
	public Player getPlayer2() {
		return _player2;
	}
	public void setPlayer2(Player player2) {
		_player2 = player2;
	}
	public Player getActivePlayer() {
		return _activePlayer;
	}
	public void setActivePlayer(Player activePlayer) {
		_activePlayer = activePlayer;
	}
	

}
