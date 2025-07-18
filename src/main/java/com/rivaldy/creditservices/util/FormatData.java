package com.rivaldy.creditservices.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatData {

    public static String percentFormat(Double data){
        NumberFormat percentFormat = NumberFormat.getPercentInstance(Locale.US);
        percentFormat.setMinimumFractionDigits(0);
        percentFormat.setMaximumFractionDigits(2);
        return percentFormat.format(data);
    }

    public static String currencyFormat(Double amount){
        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        symbols.setCurrencySymbol("Rp. ");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setMonetaryGroupingSeparator('.');

        df.setDecimalFormatSymbols(symbols);
        return df.format(amount);
    }
}
