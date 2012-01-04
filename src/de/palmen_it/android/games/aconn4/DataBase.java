package de.palmen_it.android.games.aconn4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.palmen_it.games.p4j.gamelogic.Board;
import de.palmen_it.games.p4j.gamelogic.Piece;
import de.palmen_it.games.p4j.gamelogic.Player;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	public DataBase(Context context) {
		super(context, "aconn4", null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS gameState "
				+ "(_id INTEGER PRIMARY KEY, "
				+ "board BLOB, "
				+ "mode INTEGER, "
				+ "difficulty INTEGER, "
				+ "message VARCHAR, "
				+ "canCancelDialog INTEGER, "
				+ "activePlayer INTEGER);");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS gameState");
		onCreate(db);
	}

	public GameState loadGameState() {
		GameState state = new GameState();
		SQLiteDatabase db = getReadableDatabase();
		
		Board board;
		Player player1, player2, activePlayer;
		String message;
		boolean canCancelDialog;
		int mode, difficulty;
		
		Cursor c = db.query("gameState", new String[] {
				"board", "mode", "difficulty", "message", "canCancelDialog", "activePlayer"},
				"_id = 0", null, null, null, null);
		if (c.getCount() != 1)
		{
			board = new Board();
			player1 = new Player(board, Piece.RED, true);
			player2 = new Player(board, Piece.YELLOW, false);
			activePlayer = player1;
			difficulty = 4;
			mode = GameState.MODE_1;
			message = null;
			canCancelDialog = false;
		}
		else
		{
			c.moveToFirst();
			
			byte[] boardData = c.getBlob(0);
			mode = c.getInt(1);
			difficulty = c.getInt(2);
			message = c.getString(3);
			canCancelDialog = c.getInt(4) == 1;
			int active = c.getInt(5);
			
			ByteArrayInputStream byteStream = new ByteArrayInputStream(boardData);
			try {
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				board = (Board)objStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				board = new Board();
			}
			
			player1 = new Player(board, Piece.RED, true);
			player2 = new Player(board, Piece.YELLOW, mode == 2);
			if (active == 1)
				activePlayer = player1;
			else
				activePlayer = player2;
		}
		
		state.setBoard(board);
		state.setPlayer1(player1);
		state.setPlayer2(player2);
		state.setMode(mode);
		state.setMessage(message);
		state.setCanCancelDialog(canCancelDialog);
		state.setActivePlayer(activePlayer);
		
		Player.setDifficulty(difficulty);
		return state;
	}
	
	public void saveGameState(GameState state) {
		SQLiteDatabase db = getWritableDatabase();
		
		byte[] boardData;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(state.getBoard());
			boardData = byteStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			boardData = new byte[0];
		}
		
		Cursor c = db.query("gameState", new String[] { "_id" }, "_id = 0",
				null, null, null, null);
		boolean isInsert = c.getCount() == 0;
		
		ContentValues values = new ContentValues(6);
		if (isInsert) values.put("_id", 0);
		values.put("board", boardData);
		values.put("mode",state.getMode());
		values.put("difficulty", Player.getDifficulty());
		values.put("message", state.getMessage());
		values.put("canCancelDialog", state.getCanCancelDialog() ? 1 : 0);
		if (state.getActivePlayer() == state.getPlayer1()) {
			values.put("activePlayer", 1);
		} else {
			values.put("activePlayer", 2);
		}
		
		if (isInsert) db.insert("gameState", null, values);
		else db.update("gameState", values, "_id = 0", null);
	}
}
