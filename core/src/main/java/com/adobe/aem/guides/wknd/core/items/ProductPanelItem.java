package com.adobe.aem.guides.wknd.core.items;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.aem.guides.wknd.core.common.utils.CommonUlti;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;

public class ProductPanelItem {
	
	private String brandName;
	
	private String productName;
	
	private String price;
	
	private String priceLabel;
	
	private String onlineOnlyImagePath;
	
	private Map<String, String> productImgInfo;
	
	@SuppressWarnings("unchecked")
	public ProductPanelItem(SlingHttpServletRequest request, ResourceResolver resourceResolver, Resource cfResource,
			Page currentPage, String productCfpath) {
		
		if (cfResource != null) {
			
			this.price = StringUtils.EMPTY;
			this.brandName = StringUtils.EMPTY;
			this.onlineOnlyImagePath = StringUtils.EMPTY;
			
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			ValueMap cfValueMap = cfResource.getValueMap();
			this.productName = CommonUlti.getDispalayName(cfValueMap);
			
			PriceInfoItem priceInfo = CommonUlti.getPriceInfo(request, currentPage, cfValueMap.get("listPrice", StringUtils.EMPTY));
			this.price = priceInfo.getPrice();
			this.priceLabel = priceInfo.getPriceLabel();
			String[] brands = cfValueMap.get("brand", String[].class);
			if(!ArrayUtils.isEmpty(brands)) {
				Tag tag = tagManager.resolve(brands[0]);
				if(tag != null) {
					this.brandName = CommonUlti.getTagtitle(tag, currentPage);
				}
			}
			String[] images = cfValueMap.get("productAssets", String[].class);
			if(!ArrayUtils.isEmpty(images)) {
				this.onlineOnlyImagePath = images[0].toString();
			}
			productImgInfo = new LinkedHashMap();
			CommonUlti.prepareProductAssetInfo(cfValueMap, productImgInfo, request, false,".transform/product-panel/image.");
			this.productImgInfo.put("productName", cfValueMap.get("productName", String.class));
		}
	}
	
	

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Map<String, String> getProductImgInfo() {
		return productImgInfo;
	}

	public void setProductImgInfo(Map<String, String> productImgInfo) {
		this.productImgInfo = productImgInfo;
	}
	
	
}
