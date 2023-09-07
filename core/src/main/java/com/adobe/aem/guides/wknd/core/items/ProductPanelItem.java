package com.adobe.aem.guides.wknd.core.items;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.aem.guides.wknd.core.common.utils.CommonUlti;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

public class ProductPanelItem {
	
	private String brandName;
	
	private String productname;
	
	private String price;
	
	private String priceLabel;
	
	private String onlineOnlyImagePath;
	
	public ProductPanelItem(SlingHttpServletRequest request, ResourceResolver resourceResolver, Resource cfResource,
			Page currentPage, String productCfpath) {
		
		if (cfResource != null) {
			
			this.price = StringUtils.EMPTY;
			this.brandName = StringUtils.EMPTY;
			
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			ValueMap cfValueMap = cfResource.getValueMap();
			this.productname = CommonUlti.getDispalayName(cfValueMap);
			
			PriceInfoItem priceInfo = CommonUlti.getPriceInfo(request, currentPage, cfValueMap.get("listPrice", StringUtils.EMPTY));
			this.price = priceInfo.getPrice();
			this.priceLabel = priceInfo.getPriceLabel();
			this.onlineOnlyImagePath = "";
		}
	}
	
	

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
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

	public String getOnlineOnlyImagePath() {
		return onlineOnlyImagePath;
	}

	public void setOnlineOnlyImagePath(String onlineOnlyImagePath) {
		this.onlineOnlyImagePath = onlineOnlyImagePath;
	}
	
	
}
