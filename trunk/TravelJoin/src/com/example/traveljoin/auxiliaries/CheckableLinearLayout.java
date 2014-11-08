package com.example.traveljoin.auxiliaries;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
	private CheckedTextView checkbox;
	
    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
	}
    
    @Override
    protected void onFinishInflate() {
    	super.onFinishInflate();
    	// find checked text view
    	RelativeLayout relativeLayout = (RelativeLayout) getChildAt(0);
		int childCount = relativeLayout.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			View view = relativeLayout.getChildAt(i);
			if (view instanceof CheckedTextView) {
				checkbox = (CheckedTextView)view;
			}
		}    	
    }
    
    @Override 
    public boolean isChecked() { 
        return checkbox != null ? checkbox.isChecked() : false; 
    }
    
    @Override 
    public void setChecked(boolean checked) {
    	if (checkbox != null) {
    		checkbox.setChecked(checked);
    	}
    }
    
    @Override 
    public void toggle() { 
    	if (checkbox != null) {
    		checkbox.toggle();
    	}
    } 
} 