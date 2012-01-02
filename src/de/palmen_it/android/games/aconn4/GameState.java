package de.palmen_it.android.games.aconn4;

import de.palmen_it.games.p4j.gamelogic.Board;
import de.palmen_it.games.p4j.gamelogic.Player;

public class GameState {
	
	public static final int MODE_1 = 1;
	public static final int MODE_2 = 2;
	public static final int MODE_1a = 3;
	
	private Board _board;
	private Player _player1;
	private Player _player2;
	private Player _activePlayer;
	private String _message;
	private int _mode;
	private boolean _canCancelDialog;
	
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
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		_message = message;
	}
	public int getMode() {
		return _mode;
	}
	public void setMode(int mode) {
		if (_player1 == null || _player2 == null) {
			throw new IllegalStateException("Players must be set before game mode");
		}
		
		_mode = mode;
		
		if (mode == MODE_1 || mode == MODE_1a) {
			_player2.setIsHuman(false);
		} else if (mode == MODE_2) {
			_player2.setIsHuman(true);
		} else {
			throw new IllegalArgumentException("Unknown mode");
		}
		_player1.setIsHuman(false);
	}
	public boolean getCanCancelDialog() {
		return _canCancelDialog;
	}
	public void setCanCancelDialog(boolean canCancelDialog) {
		_canCancelDialog = canCancelDialog;
	}
	
	public void togglePlayer() {
		if (_activePlayer == _player1)
			_activePlayer = _player2;
		else
			_activePlayer = _player1;
	}
}
