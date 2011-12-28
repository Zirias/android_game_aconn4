package de.palmen_it.android.games.aconn4;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class AConn4Layout extends FrameLayout {

	private Button[] _buttons;
	private TableLayout _table;
	
	public AConn4Layout(Context context) {
		super(context);
		
        _table = new TableLayout(context);
        TableRow buttonRow = new TableRow(context);        
        buttonRow.setId(107);

        _buttons = new Button[7];
        
        for (int col = 0; col < 7; ++col) {
        	Button b = new Button(context);
        	b.setText("O");
        	b.setId(col + 100);
        	_buttons[col] = b;
        	buttonRow.addView(b);
        }
        _table.addView(buttonRow);        

        addView(_table);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
        int totalSize, marginTop, marginLeft, tileSize, additionalMargin;
        if (h > w) {
        	totalSize = w;
        	marginLeft = 0;
        	marginTop = (h - totalSize) / 2;
        } else {
        	totalSize = h;
        	marginTop = 0;
        	marginLeft = (w - totalSize) / 2;
        }
        tileSize = totalSize / 7;
        additionalMargin = (totalSize % 7) / 2;
        marginTop += additionalMargin;
        marginLeft += additionalMargin;
        totalSize = tileSize * 7;
       
        setPadding(marginLeft, marginTop, 0, 0);
        
        _table.setLayoutParams(new LayoutParams(totalSize, totalSize));
        for (Button b: _buttons) {
        	b.setLayoutParams(new TableRow.LayoutParams(tileSize, tileSize));
        }
	}
}
