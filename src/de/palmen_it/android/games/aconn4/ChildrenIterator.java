package de.palmen_it.android.games.aconn4;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.View;
import android.view.ViewGroup;

public class ChildrenIterator<V extends View> implements Iterator<V> {

	private ArrayList<V> _list;
	private int _i;
	
	public ChildrenIterator(ViewGroup vg) {
		super();
		if (vg == null) {
			throw new RuntimeException("ChildrenIterator needs a ViewGroup != null to find its children");
		}
		
		_list = new ArrayList<V>();
		_i = 0;
		findChildrenAndAddToList(vg, _list);
	}
	
	public boolean hasNext() {
		return _i < _list.size();
	}

	public V next() {
		return _list.get(_i++);
	}

	public void remove() {
		_list.remove(_i);
	}

	@SuppressWarnings("unchecked")
	private void findChildrenAndAddToList(final ViewGroup root, final ArrayList<V> list) {
		for (int i=0; i < root.getChildCount(); i++) {
			V v = (V)root.getChildAt(i);
			list.add(v);
			if (v instanceof ViewGroup) {
				findChildrenAndAddToList((ViewGroup)v, list);
			}
		}
	}
}
