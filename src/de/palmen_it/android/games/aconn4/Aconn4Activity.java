package de.palmen_it.android.games.aconn4;

import android.app.Activity;
import android.os.Bundle;

public class Aconn4Activity extends Activity {
	
	private AConn4Layout _layout;
	private GameState _state;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _layout = new AConn4Layout(this);
        setContentView(_layout);
        Object state = getLastNonConfigurationInstance();
        if (state == null) {
        	_state = new GameState();
        } else {
        	_state = (GameState) state;
        }
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
    	return _state;
    }
}