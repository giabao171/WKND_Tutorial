package com.adobe.aem.guides.wknd.core.items;

/**
 * 
 * 
 */

public class PriceInfoItem {
	
	private String price;	
	private String priceLabel;

	public PriceInfoItem(String price, String priceLabel) {
		super();
		this.price = price;
		this.priceLabel = priceLabel;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}
	
	
}
