package de.palmen_it.android.games.aconn4;

import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class GameDialog extends AlertDialog{
	
	protected GameDialog(Context context, int theme) {
		super(context, theme);
	}

	protected GameDialog(Context context) {
		super(context);
	}

	public void show()
	{
		super.show();
		makeTranslucent();
	}

	private void makeTranslucent() {
		
			Iterator<View> vi = iterator(android.R.id.content);
			while (vi.hasNext()) {
				Drawable bg = vi.next().getBackground();
				if (bg != null)
				{
					bg.mutate().setAlpha(0x66);
				}
			}
	}
	
	public Iterator<View> iterator(int res) {
		final ViewGroup vg = (ViewGroup) findViewById(res);
		return new ChildrenIterator<View>(vg);
	}
	
	public static class Builder extends AlertDialog.Builder {
		private GameDialog _dialog;
		
		public Builder(Context context) {
			super(context);
			_dialog = new GameDialog(context);
		}
		
		public Builder(Context context, int theme) {
			super(context);
			_dialog = new GameDialog(context, theme);
		}
		
		@Override
		public GameDialog create() {
			return _dialog;
		}
		
		@Override
		public Builder setMessage(CharSequence message) {
			_dialog.setMessage(message);
			return this;
		}

		@Override
		public Builder setTitle(CharSequence message) {
			_dialog.setTitle(message);
			return this;
		}
		
		@Override
		public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
			_dialog.setButton(BUTTON_POSITIVE, text, listener);
			return this;
		}
		
		@Override
		public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
			_dialog.setButton(BUTTON_NEGATIVE, text, listener);
			return this;
		}

		@Override
		public Builder setIcon(int iconId) {
			_dialog.setIcon(iconId);
			return this;
		}
	}
}
