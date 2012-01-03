package de.palmen_it.android.games.aconn4;

import de.palmen_it.games.p4j.gamelogic.AITaskDescriptor;
import de.palmen_it.games.p4j.gamelogic.Player;
import android.os.AsyncTask;

public class AITask extends AsyncTask<Player, Void, Void> {

	static class Descriptor implements AITaskDescriptor {
		private boolean _isCancelled = false;
		
		public boolean isCancelled() {
			return _isCancelled;
		}
		
		public void cancel() {
			_isCancelled = true;
		}
	}
	private final Aconn4EventListener _listener;
	private Descriptor _descriptor;
	
	public AITask(Aconn4EventListener listener) {
		_listener = listener;
		_descriptor = new Descriptor();
	}
	
	@Override
	protected Void doInBackground(Player... players) {
		Player p = players[0];
		
		if (p.getIsHuman()) {
			p.getBestColumns(_descriptor);
		} else {
			p.move(_descriptor);
		}
		
		return null;
	}

	public void cancel() {
		_descriptor.cancel();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		_listener.onAiDone(_descriptor.isCancelled());
	}
}
