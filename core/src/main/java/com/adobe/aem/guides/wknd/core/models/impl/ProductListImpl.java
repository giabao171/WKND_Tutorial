package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.ProductList;
import com.drew.lang.annotations.Nullable;

@Model(adaptables = {SlingHttpServletRequest.class},
		adapters = {ProductList.class},
		resourceType = ProductListImpl.RESOURCE,
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class ProductListImpl implements ProductList{
	
	protected static final String RESOURCE = "wknd/components/product-list";
	
	private static final Logger LOG = LoggerFactory.getLogger(ProductListImpl.class);
	
	@SlingObject
	private ResourceResolver resourceResolver;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String[] cfPaths;

	@Override
	public List<String> getCfPathsList() {
		
		List<String> cfPathsList = new ArrayList<String>();
		try {
			for(String cfPath: cfPaths) {
				Resource cFResource = resourceResolver.getResource(cfPath + "/jcr:content/data/master");
				if(cFResource != null) {
					cfPathsList.add(cfPath);
				}
			}
		} catch (Exception e) {
			LOG.error("product List Error: {}", e.getMessage());		
		}		
		return cfPathsList;
	}

	@Override
	public String[] getCfPaths() {
		return cfPaths;
	}

}
