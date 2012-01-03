package de.palmen_it.android.games.aconn4;

import java.util.ArrayList;

import de.palmen_it.games.p4j.gamelogic.Piece;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

public class AConn4Layout extends LinearLayout implements OnClickListener, OnSeekBarChangeListener {

	private static final int ID_RESTART = 101;
	private static final int ID_MODE_1 = 102;
	private static final int ID_MODE_2 = 103;
	private static final int ID_MODE_1a = 104;
	
	private Button[] _buttons;
	private ImageView[][] _fields;
	private TableLayout _table;
	private LinearLayout _controls;
	private Button _restart;	
	private RadioGroup _mode;
	private RadioButton _mode1;
	private RadioButton _mode2;
	private RadioButton _mode1a;
	private TextView _difficultyView;
	private SeekBar _difficultySet;
	
	private Drawable _empty;
	private Drawable _red;
	private Drawable _yellow;
	
	private ArrayList<Aconn4EventListener> _eventListeners;

	public AConn4Layout(Context context) {
		super(context);
		
		_eventListeners = new ArrayList<Aconn4EventListener>();
		
		_empty = getResources().getDrawable(R.drawable.empty);
		_red = getResources().getDrawable(R.drawable.red);
		_yellow = getResources().getDrawable(R.drawable.yellow);
		
        _table = new TableLayout(context);
        TableRow tr = new TableRow(context);        

        _buttons = new Button[7];
        
        for (int col = 0; col < 7; ++col) {
        	Button b = new Button(context);
        	b.setText("O");
        	b.setId(col);
        	b.setOnClickListener(this);
        	_buttons[col] = b;
        	tr.addView(b);
        }
        _table.addView(tr);        

        _fields = new ImageView[6][7];
        
        for (int row = 0; row < 6; ++row) {
        	tr = new TableRow(context);
        	for (int col = 0; col < 7; ++col) {
        		ImageView v = new ImageView(context);
        		v.setImageDrawable(_empty);
        		_fields[row][col] = v;
        		tr.addView(v);
        	}
        	_table.addView(tr);
        }
        
        _controls = new LinearLayout(context);
        _controls.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        _controls.setOrientation(LinearLayout.VERTICAL);
        
        TextView t = new TextView(context);
        LayoutParams marginParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        marginParams.setMargins(5, 5, 5, 5);
        t.setLayoutParams(marginParams);
        t.setTextSize(18);
        t.setTextColor(Color.WHITE);
        t.setBackgroundColor(Color.DKGRAY);
        t.setText("Options");
        t.setGravity(Gravity.CENTER);
        _controls.addView(t);
        
        t = new TextView(context);
        t.setLayoutParams(marginParams);
        t.setText("Mode:");
        _controls.addView(t);
        
        _mode = new RadioGroup(context);
        _mode.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        _mode.setOrientation(LinearLayout.HORIZONTAL);
  
        _mode1 = new RadioButton(context);
        _mode1.setText("1 Player");
        _mode1.setId(ID_MODE_1);
        _mode1.setOnClickListener(this);
        _mode.addView(_mode1);
        _mode1a = new RadioButton(context);
        _mode1a.setText("assisted");
        _mode1a.setId(ID_MODE_1a);
        _mode1a.setOnClickListener(this);
        _mode.addView(_mode1a);
        _mode2 = new RadioButton(context);
        _mode2.setText("2 Player");
        _mode2.setId(ID_MODE_2);
        _mode2.setOnClickListener(this);
        _mode.addView(_mode2);
        
        _controls.addView(_mode);

        t = new TextView(context);
        t.setLayoutParams(marginParams);
        t.setText("Strength: ");
        _controls.addView(t);
        
        LinearLayout l = new LinearLayout(context);
        l.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        l.setOrientation(LinearLayout.HORIZONTAL);
        
        _difficultyView = new TextView(context);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(32, LayoutParams.WRAP_CONTENT);
        dlp.setMargins(5, 5, 5, 5);
        _difficultyView.setLayoutParams(dlp);
        _difficultyView.setGravity(Gravity.RIGHT);
        l.addView(_difficultyView);
        
        _difficultySet = new SeekBar(context);
        _difficultySet.setLayoutParams(marginParams);
        _difficultySet.setPadding(5, 5, 32, 5);
        _difficultySet.setMax(10);
        _difficultySet.setOnSeekBarChangeListener(this);
        _difficultySet.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        l.addView(_difficultySet);
        
        _controls.addView(l);
        
        marginParams.setMargins(5, 12, 5, 0);
        _restart = new Button(context);
        _restart.setLayoutParams(marginParams);
        _restart.setText("Restart Game");
        _restart.setOnClickListener(this);
        _restart.setId(ID_RESTART);
        
        _controls.addView(_restart);
        
        addView(_table);
        addView(_controls);
	}

	public void setPieceAt(Piece p, int row, int column) {
		switch (p) {
		case RED:
			_fields[row][column].setImageDrawable(_red);
			break;
		case YELLOW:
			_fields[row][column].setImageDrawable(_yellow);
			break;
		default:
			_fields[row][column].setImageDrawable(_empty);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
        int totalSize, tileSize;
        if (h > w) {
        	totalSize = w;
        	setOrientation(LinearLayout.VERTICAL);
        } else {
        	totalSize = h;
        	setOrientation(LinearLayout.HORIZONTAL);
        }
        tileSize = totalSize / 7;
        totalSize = tileSize * 7;
       
        _table.setLayoutParams(new LayoutParams(totalSize, totalSize));
        for (Button b: _buttons) {
        	b.setLayoutParams(new TableRow.LayoutParams(tileSize, tileSize));
        }
        
        for (ImageView[] r: _fields) {
        	for (ImageView v: r) {
        		v.setLayoutParams(new TableRow.LayoutParams(tileSize, tileSize));
        	}
        }
	}

	public void setAconn4EventListener(Aconn4EventListener l) {
		_eventListeners.add(l);
	}
	
	public void setButtonsEnabled(boolean enabled) {
		for (Button b: _buttons) {
			b.setEnabled(enabled);
		}
	}
	
	public void setRestartEnabled(boolean enabled) {
		_restart.setEnabled(enabled);
	}
	
	public void setOptionsEnabled(boolean enabled) {
		_mode1.setEnabled(enabled);
		_mode2.setEnabled(enabled);
		_mode1a.setEnabled(enabled);
		_difficultySet.setEnabled(enabled);
	}
	
	public void setMode(int mode) {
		if (mode == GameState.MODE_1) {
			_mode1.setChecked(true);
		} else if (mode == GameState.MODE_1a) {
			_mode1a.setChecked(true);
		} else if (mode == GameState.MODE_2) {
			_mode2.setChecked(true);
		} else {
			throw new IllegalArgumentException("Unknown game mode!");
		}
	}
	
	public int getMode() {
		if (_mode2.isChecked()) return GameState.MODE_2;
		if (_mode1a.isChecked()) return GameState.MODE_1a;
		return GameState.MODE_1;
	}

	public void onClick(View v) {
		int id = v.getId();
		for (Aconn4EventListener l: _eventListeners) {
			if (id == ID_MODE_1) {
				l.onModeChange(GameState.MODE_1);
			} else if (id == ID_MODE_1a) {
				l.onModeChange(GameState.MODE_1a);
			} else if (id == ID_MODE_2) {
				l.onModeChange(GameState.MODE_2);
			} else if (id == ID_RESTART) {
				l.onRestartGame();
			} else {
				l.onInsert(id);
			}
		}
	}

	public void setDifficulty(int difficulty) {
		_difficultySet.setProgress(difficulty - 2);
		_difficultyView.setText(String.valueOf(difficulty));
	}
	
	public int getDifficulty() {
		return _difficultySet.getProgress() + 2;
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (!fromUser) return;
		for (Aconn4EventListener l: _eventListeners) {
			l.onDifficultyChange(progress);
		}
		_difficultyView.setText(String.valueOf(progress + 2));
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
