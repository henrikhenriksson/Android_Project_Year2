package se.miun.hehe0601.dt031g.bathingsites;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * TODO: document your custom view class.
 */
public class BathingSitesView extends ConstraintLayout {

    private TextView bathingSites;
    private int bathSiteCounter = 0;

    public BathingSitesView(Context context) {
        super(context);
        init(null);
    }

    public BathingSitesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BathingSitesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

        inflate(getContext(), R.layout.bathing_sites_view, this);
        bathingSites = findViewById(R.id.bathingSiteText);

        // Get the ammount of BathingSites
        // value for debugging:
        setBathingSites();


    }

    public int getBathSiteCounter() {
        return bathSiteCounter;
    }

    public void setBathSiteCounter(int bathSiteCounter) {
        this.bathSiteCounter = bathSiteCounter;
        setBathingSites();
    }

    public void setBathingSites() {
        bathingSites.setText(bathSiteCounter + " Bathing Sites");
    }
    //    private void invalidateTextPaintAndMeasurements() {
//        mTextPaint.setTextSize(mExampleDimension);
//        mTextPaint.setColor(mExampleColor);
//        mTextWidth = mTextPaint.measureText(mExampleString);
//
//        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
//        mTextHeight = fontMetrics.bottom;

//    // Load attributes
//    final TypedArray a = getContext().obtainStyledAttributes(
//            attrs, R.styleable.BathingSitesView, 0, 0);
//
//
//        a.recycle();
////    }

}
