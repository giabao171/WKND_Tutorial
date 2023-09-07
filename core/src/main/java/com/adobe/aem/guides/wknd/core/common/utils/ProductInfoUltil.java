package com.adobe.aem.guides.wknd.core.common.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductInfoUltil {
	
	public static String formatNumberBasedOnLocale(Locale locale, double property) {

		return formatNumberBasedOnLocale(locale, property, true);
	}

	public static String formatNumberBasedOnLocale(Locale locale, double property, boolean isPrice) {

		NumberFormat nf = NumberFormat.getInstance(locale);
		return nf.format(property);
	}
}
