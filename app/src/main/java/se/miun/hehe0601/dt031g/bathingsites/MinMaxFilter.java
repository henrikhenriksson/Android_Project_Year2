package se.miun.hehe0601.dt031g.bathingsites;

/*
 * FileName: MinMaxFilter.java
 * Author: Henrik Henriksson (hehe0601)
 * Description This file contains the min/max filter class used to validate user input
 * Course: DT031G project at Miun, spring 2020
 * Since: 2020-05-12
 */

import android.text.InputFilter;
import android.text.Spanned;


// Courtesy of https://www.tutorialspoint.com/how-to-define-a-min-and-max-value-for-edittext-in-android
public class MinMaxFilter implements InputFilter {

    private double mDoubleMax;
    private double mDoubleMin;

    public MinMaxFilter(double minVal, double maxVal) {
        this.mDoubleMin = minVal;
        this.mDoubleMax = maxVal;

    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(mDoubleMin, mDoubleMax, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "Wrong Format";
    }

    private boolean isInRange(double min, double max, double input) {
        return max > min ? input >= min && input <= max : input >= max && input <= min;
    }
}
