package de.palmen_it.android.games.aconn4;

import de.palmen_it.games.p4j.gamelogic.Board;
import de.palmen_it.games.p4j.gamelogic.Piece;
import de.palmen_it.games.p4j.gamelogic.Player;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class Aconn4Activity extends Activity implements Aconn4EventListener {
	
	private AConn4Layout _layout;
	private GameState _state;
	private DataBase _db;
	private AITask _ai;
	
	private static void updateLayoutFromBoard(AConn4Layout l, Board b) {
        for (int row = 0; row < 6; ++row) {
        	for (int col = 0; col < 7; ++col) {
        		l.setPieceAt(b.getPieceAt(row, col), row, col);
        	}
        }
        if (b.getNumberOfInserts() == 0) {
        	l.setRestartEnabled(false);
        	l.setOptionsEnabled(true);
        } else {
        	l.setRestartEnabled(true);
        	l.setOptionsEnabled(false);
        }
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _layout = new AConn4Layout(this);
        _layout.setAconn4EventListener(this);
        setContentView(_layout);
        _db = new DataBase(this);
        _state = _db.loadGameState();
        updateLayoutFromBoard(_layout, _state.getBoard());
        _layout.setMode(_state.getMode());
        _layout.setDifficulty(Player.getDifficulty());
        String message = _state.getMessage();
        if (message != null) showGameDialog(message, _state.getCanCancelDialog());
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	_db.saveGameState(_state);
    }
    
    private void showGameDialog(String message, boolean allowCancel) {
    	_state.setMessage(message);
    	_state.setCanCancelDialog(allowCancel);
		GameDialog.Builder builder = new GameDialog.Builder(this);
		builder.setTitle(message)
			.setPositiveButton("Ok", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					_state.setMessage(null);
					restartGame();
				}
			});
		if (allowCancel) builder.setNegativeButton("Cancel", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				_state.setMessage(null);
			}
		});
		GameDialog dlg = builder.create();
		dlg.show();
    }
    
    private boolean checkWinner() {
    	if (_state.getBoard().getNumberOfInserts() == 42) {
    		showGameDialog("Draw game!", false);
    		return true;
    	}
    	Piece playerPiece = _state.getActivePlayer().getPiece();
    	Piece winningPiece = _state.getBoard().getRows(0).getWinner();
    	if (winningPiece == playerPiece) {
    		showGameDialog(winningPiece.toString() + " wins!", false);
    		return true;
    	}
    	return false;
    }
    
	public void onInsert(int column) {
		Player p = _state.getActivePlayer();
		if (p.move(column)) {
			_layout.setPieceAt(p.getPiece(), p.getLastRow(), p.getLastColumn());
			if (checkWinner()) return;
			_state.togglePlayer();
			p = _state.getActivePlayer();
			_layout.setRestartEnabled(true);
			_layout.setOptionsEnabled(false);
			if (p.getIsHuman()) return;
			_layout.setButtonsEnabled(false);
			_ai = new AITask(this);
			_ai.execute(p);
		}
	}

	public void onModeChange(int mode) {
		_state.setMode(mode);
	}
	
	private void restartGame() {
		if (_ai != null) {
			_ai.cancel();
			return;
		}
		_layout.setButtonsEnabled(true);
		Board b = _state.getBoard();
		b.clear();
		_state.setActivePlayer(_state.getPlayer1());
		updateLayoutFromBoard(_layout, b);		
	}
	
	public void onRestartGame() {
		showGameDialog("Do you really want to restart?", true);
	}
	
	public void onAiDone(boolean isCancelled) {
		_ai = null;
		if (isCancelled) {
			restartGame();
			return;
		}
		Player p = _state.getActivePlayer();
		_layout.setPieceAt(p.getPiece(), p.getLastRow(), p.getLastColumn());
		_layout.setButtonsEnabled(true);		
		if (checkWinner()) return;
		_state.togglePlayer();
	}

	public void onDifficultyChange(int difficulty) {
		Player.setDifficulty(difficulty);
	}

}