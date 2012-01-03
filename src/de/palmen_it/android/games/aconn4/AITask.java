package de.palmen_it.android.games.aconn4;

import de.palmen_it.games.p4j.gamelogic.AITaskDescriptor;
import de.palmen_it.games.p4j.gamelogic.Player;
import android.os.AsyncTask;

public class AITask extends AsyncTask<Player, Void, Void> implements AITaskDescriptor {

	private final Aconn4EventListener _listener;
	
	public AITask(Aconn4EventListener listener) {
		_listener = listener;
	}
	
	@Override
	protected Void doInBackground(Player... players) {
		Player p = players[0];
		
		if (p.getIsHuman()) {
			p.getBestColumns(this);
		} else {
			p.move(this);
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		_listener.onAiDone();
	}
}
