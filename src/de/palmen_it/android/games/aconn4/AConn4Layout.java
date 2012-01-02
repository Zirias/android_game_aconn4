package de.palmen_it.android.games.aconn4;

import java.util.ArrayList;

import de.palmen_it.games.p4j.gamelogic.Piece;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.view.View;
import android.view.View.OnClickListener;

public class AConn4Layout extends LinearLayout implements OnClickListener {

	private static final int RESTART = 101;
	
	private Button[] _buttons;
	private ImageView[][] _fields;
	private TableLayout _table;
	private LinearLayout _controls;
	private Button _restart;
	
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
        
        _restart = new Button(context);
        _restart.setText("Restart Game");
        _restart.setOnClickListener(this);
        _restart.setId(RESTART);
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
		_restart.setEnabled(enabled);
	}
	
	public void onClick(View v) {
		int id = v.getId();
		for (Aconn4EventListener l: _eventListeners) {
			if (id == RESTART) {
				l.onRestartGame();
			} else {
				l.onInsert(id);
			}
		}
	}
}
