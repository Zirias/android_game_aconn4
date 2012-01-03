package de.palmen_it.android.games.aconn4;

public interface Aconn4EventListener {
	void onInsert(int column);
	void onRestartGame();
	void onAiDone(boolean isCancelled);
	void onModeChange(int mode);
	void onDifficultyChange(int difficulty);
}
