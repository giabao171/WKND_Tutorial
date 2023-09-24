package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.common.utils.CommonUlti;
import com.adobe.aem.guides.wknd.core.caconfig.TextConfig;
import com.adobe.aem.guides.wknd.core.items.ProductPanelItem;
import com.adobe.aem.guides.wknd.core.models.ProductPanel;
import com.day.cq.wcm.api.Page;


@Model(adaptables = {SlingHttpServletRequest.class},
		adapters = {ProductPanel.class},
		resourceType = {ProductPanelImpl.RESOURCE},
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class ProductPanelImpl implements ProductPanel{
	
	protected static final String RESOURCE = "wknd/components/product-panel";
	
	private static final Logger LOG = LoggerFactory.getLogger(ProductPanelImpl.class);
	
	@Self
	SlingHttpServletRequest request;
	
	@ScriptVariable
	private Page currentPage ;
	
	@SlingObject
	private ResourceResolver resourceResolver;
	
	ProductPanelItem productPanelItem;
	
	TextConfig textConfig;
	//com.adobe.aem.guides.wknd.core.config.TextConfig textConfig2;
	
	@PostConstruct
	protected void initModel() {
		
		String productCfPath = request.getRequestPathInfo().getSelectors()[0];
		RequestPathInfo productCfPath1 = request.getRequestPathInfo();
		/*
		 * textConfig = CommonUlti.getCaConfiguration(resourceResolver, currentPage,
		 * TextConfig.class); String text = textConfig.caConfigTexting();
		 */
		
		textConfig = CommonUlti.getCaConfiguration(resourceResolver, currentPage, TextConfig.class);
		String text2 = textConfig.caConfigTexting();
		
		Resource cfResource = resourceResolver.getResource(productCfPath +"/jcr:content/data/master");
		productPanelItem = new ProductPanelItem(request, resourceResolver, cfResource, currentPage, productCfPath);	
		LOG.error("/n product panel init: " + productCfPath);
	}

	@Override
	public ProductPanelItem getProductPanelInfo() {
		
		return productPanelItem;
	}

}
