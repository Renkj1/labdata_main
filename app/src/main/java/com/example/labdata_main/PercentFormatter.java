package com.example.labdata_main;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class PercentFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return String.format("%.1f%%", value);
    }
}
