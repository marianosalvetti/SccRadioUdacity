package ar.com.sccradiomobile.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import ar.com.sccradiomobile.livescores.FontCache;

/**
 * Created by Mariano Salvetti
 *
 * from http://slothdevelopers.wordpress.com/2014/05/11/custom-fonts-in-textview-and-fontcache/
 */
public class RobotoTextView extends TextView {

    public RobotoTextView(Context context) {
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (!this.isInEditMode()) {
            if (style == Typeface.NORMAL) {
                super.setTypeface(FontCache.getFont(getContext(), "fonts/Roboto-Regular.ttf"));
            } else if (style == Typeface.ITALIC) {
                super.setTypeface(FontCache.getFont(getContext(), "fonts/Roboto-Italic.ttf"));
            } else if (style == Typeface.BOLD) {
                super.setTypeface(FontCache.getFont(getContext(), "fonts/Roboto-Bold.ttf"));
            }
        }
    }
}