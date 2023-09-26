package com.adobe.aem.guides.wknd.core.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.caconfig.TextConfig;
import com.adobe.aem.guides.wknd.core.items.PriceInfoItem;
import com.day.cq.i18n.I18n;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.drew.lang.annotations.NotNull;
import com.google.gson.Gson;

public class CommonUlti {

	private static final Logger LOG = LoggerFactory.getLogger(CommonUlti.class);
	
	public static <T> T getCaConfiguration(ResourceResolver resolver, Page currentPage, Class<T> paramClass) {
		
		return resolver.getResource(currentPage.getPath()).adaptTo(ConfigurationBuilder.class).as(paramClass);
	}
	
	public static String getTextCaConfig(ResourceResolver resolver, Page currentPage) {
		
		return resolver.getResource(currentPage.getPath()).adaptTo(ConfigurationBuilder.class).as(TextConfig.class).caConfigTexting();
	}

	@NotNull
	public static String getLink(@NotNull SlingHttpServletRequest request, @NotNull Page page, ResourceResolver resourceReslover, Resource resource) {

		String redirectTargetpath = page.getProperties().get("cq:redirectTarget", String.class);
		String url = redirectTargetpath;

		if (StringUtils.isEmpty(redirectTargetpath))
			url = page.getPath();

		if (url.startsWith("/content/dam")) {
			url = request.getContextPath() + url;
		} else if (url.startsWith("/content") && !url.endsWith(".html")) {
			url = request.getContextPath() + url + ".html";
		}
		return url;
	}

	public static List<String> getContentPanelList(Resource resourceList, int contentPanelNumber, SlingHttpServletRequest request) {

		List<String> listContentPanelReturn = new ArrayList<String>();
		ResourceResolver resourceReslover = resourceList.getResourceResolver();
		Node contentPanelListNode = resourceList.adaptTo(Node.class);

		if (contentPanelListNode != null) {
			try {
				int i = 0;
				String nodeName;
				Resource contentPanel;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				NodeIterator contentPanelListNodes = contentPanelListNode.getNodes();
				
				Map<String, Object> options = new HashMap<>();
				options.put("sling:resourceType", "wknd/components/content-panel");
				options.put("jcr:primaryType", "nt:unstructured");
				
				// if content panel list has content panel 
				while (i < contentPanelNumber && contentPanelListNodes.hasNext()) {
					Node contentpanelNodeChild = contentPanelListNodes.nextNode();
					ValueMap childMap = resourceReslover.getResource(contentpanelNodeChild.getPath()).getValueMap();
					if(childMap.get("sling:resourceType", StringUtils.EMPTY).equals("wknd/components/content-panel")) {
						listContentPanelReturn.add(contentpanelNodeChild.getName());
						i++;
					}else if(childMap.get("sling:resourceType", StringUtils.EMPTY).equals("wcm/msm/components/ghost")) {
						if(WCMMode.EDIT.equals(WCMMode.fromRequest(request))) {
							listContentPanelReturn.add(contentpanelNodeChild.getName());
						}
					}
				}
				
				//create new content panel in contnent panel list
				try {
					if(WCMMode.EDIT.equals(WCMMode.fromRequest(request))) {
						while(i < contentPanelNumber) {
							nodeName = "content_panel_bao_"+ dateFormat.format(new Date()) + Integer.toString(i);
							contentPanel = resourceReslover.create(resourceList, nodeName, options);
							listContentPanelReturn.add(contentPanel.getName());
							i++;
						}
						resourceReslover.commit();
					}
				} catch (Exception e) {
					LOG.error("There is error when create content-panel node{}", e.getMessage());
				}
			} catch (Exception e) {
				LOG.error("There is error {}", e.getMessage());
			}
		}

		return listContentPanelReturn;
	}
	
	public static String getDispalayName(ValueMap valueMap) {
		
		String dispalyproductName = StringUtils.EMPTY;
		if(valueMap != null) {
			dispalyproductName = valueMap.get("productName", "");
			if(StringUtils.isBlank(dispalyproductName)) {
				dispalyproductName = "Default Product";
			}
		}
		return dispalyproductName;
	}
	
	public static PriceInfoItem getPriceInfo(SlingHttpServletRequest request, Page currentPage, String listPrice) {

		String price = StringUtils.EMPTY;
		String priceLabel = StringUtils.EMPTY;
		try {
			Locale locale = currentPage.getLanguage();
			ResourceBundle bundle = request.getResourceBundle(locale);
			I18n i18n = new I18n(bundle);

			// Get price info base on i18n
			if (StringUtils.isNotEmpty(listPrice)) {
				Double priceNum = Double.valueOf(listPrice);
				price = i18n.get("Coming soon", "casio");
				if (priceNum > 0) {
					price = i18n.get("${0}", "casio",ProductInfoUltil.formatNumberBasedOnLocale(locale, priceNum));
					priceLabel = i18n.get("MSRP", "casio");
				}
			}
		} catch (NumberFormatException e) {
			price = StringUtils.EMPTY;
			priceLabel = StringUtils.EMPTY;
		}
		return new PriceInfoItem(price, priceLabel);
	}
	
	public static String getTagtitle(Tag tag, Page page) {
		
		if(page == null) {
			return tag.getTitle();
		}
		Locale tagLocale = new Locale(getLanguageCode(page, false));
		String tagTitle = tag.getLocalizedTitle(tagLocale);
		if(tagTitle ==null) {
			tagTitle = tag.getTitle(page.getLanguage());
		}
		return tagTitle;
	}
	
	public static String getLanguageCode(Page currentPage, boolean ignoreContent) {
		
		String langageCode = currentPage.getLanguage(ignoreContent).toString().toLowerCase().replace("_", "-");
		if (ignoreContent && "in".equals(langageCode)) {
			langageCode = "id";
		}
		return langageCode;
	}
	
	public static void prepareProductAssetInfo(ValueMap valueMap, Map<String, String> assetInfo, SlingHttpServletRequest request, 
			boolean useTitleProperty, String transformUrl) {
		
		String path = StringUtils.EMPTY;
		String alt = StringUtils.EMPTY;
		final String[] productAssets = valueMap.get("productAssets", String[].class);
		if(productAssets != null && productAssets.length > 0) {
			final Map<String, String> productImgInfo = CommonUlti.getProductImgInfo(productAssets[0]);
			if(productImgInfo != null) {
				path = productImgInfo.get("path");
				/*
				 * if(!path.isEmpty()) { path += transformUrl +
				 * FilenameUtils.getExtension(path); }
				 */
				alt = productImgInfo.get("alt");
			}
		}
		assetInfo.put(useTitleProperty ? "title" : "alt", alt);
		assetInfo.put("path", path);		
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getProductImgInfo(String productAssets) {
		
		Map<String, String> productInfo = null;
		try {
			productInfo = (new Gson()).fromJson(productAssets, Map.class);
		} catch (Exception e) {
			LOG.error("There is error in getProductImgInfo Json {}", e.getMessage());
		}
		return productInfo;
	}
	
	public static String getproductPagepath(Page currentpage, ValueMap cfValuemap, TagManager tagManager) {
		
		String productPagePath = null;
		String languagePagepath = null;
		String currentPagepath = currentpage.getPath();
		String language = "/"+ getLanguageCode(currentpage, false);
		
		// Set languagePagePath to currentPagePath when currentPagePath end with language. Ex: "/content/wknd/jp/ja"
		if(currentPagepath.endsWith(language) && !currentPagepath.equals("/content/wknd")) {
			languagePagepath = currentPagepath;
		}
		else {
			// Index of language in the case country and language codes are same. ("/th/th/", "/id/id/")
			int languageIndex = currentPagepath.indexOf(language + language + "/");
			if(languageIndex != -1) {
				// Cut current page path from begin to country position into languagePagePath
				// Ex: /content/wknd/jp/ja/product
				// ==> /content/wknd/jp/ja
				languagePagepath = currentPagepath.substring(0, languageIndex + (language.length() * 2));
			}
			else {
				// Index of language string ("/ja/", "/en/") in the current page path
				// To prevent the language is contained in node name of page we need add slash before checking
				languageIndex = currentPagepath.indexOf(language+"/");
				if(languageIndex != -1) {
					// Cut current page path from begin to language position into languagePagePath
					// Ex: /content/wknd/jp/ja/product 
					// ==> /content/wknd/jp/ja
					languagePagepath = currentPagepath.substring(0, languageIndex + language.length());
				}
			}
 		}
		
		if(languagePagepath != null) {
			String[] brands = cfValuemap.get("brand", String[].class);
			if(ArrayUtils.isNotEmpty(brands)) {
				Tag tag= tagManager.resolve(brands[0]);
				if(tag != null) {
					Resource tagResource = tag.adaptTo(Resource.class);
					
					String productPagepathBrand = tagResource.getValueMap().get("productPagePath", StringUtils.EMPTY);
					if(productPagepathBrand != null) {
						String productName = cfValuemap.get("productName", StringUtils.EMPTY);
						productPagePath = languagePagepath + productPagepathBrand + "/product." + productName + ".html";
					}
				}
			}
		}
		return productPagePath;
	}
}
