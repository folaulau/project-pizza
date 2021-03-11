package com.lovemesomecoding.pizzaria.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class MathUtils {

	//	Get Two Decimal String
	public static BigDecimal getStrTwoDecimalPlaces(String value) {
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	// Get Two Decimal Double
	public static Double getTwoDecimalPlaces(Double value) {
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	public static Double getTwoDecimalPlaces(Long value) {
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
	}
	
	public static String getTwoDecimalPlacesAsString(Double value) {
		double amount = new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(amount);
	}
	
	public static String formatDollarAmountWithTwoDecimalAsString(double amount) {
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(amount);
	}
	
	public static Float getTwoDecimalPlacesAsFloat(Double value) {
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN).floatValue();
	}

	public static int getCentsForDollars(Double amountPaid) {
		
		return (amountPaid!=null && amountPaid>0) ? (int)(amountPaid*100) : 0;
	}

	public static double getDollarsFromCents(Long amount) {
		// TODO Auto-generated method stub
		return (double)(amount/100);
	}
}
