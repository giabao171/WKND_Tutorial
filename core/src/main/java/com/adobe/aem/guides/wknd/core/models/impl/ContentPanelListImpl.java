package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.common.utils.CommonUlti;
import com.adobe.aem.guides.wknd.core.models.ContentPanelList;
import com.day.cq.wcm.models.annotations.injectorspecific.StyleOrValueMapValue;
import com.drew.lang.annotations.NotNull;

@Model(adaptables = {SlingHttpServletRequest.class},
		adapters = {ContentPanelList.class},
		resourceType = {ContentPanelImpl.RESOURCE_TYPE},
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class ContentPanelListImpl implements ContentPanelList{
	
	protected static final String RESOURCE_TYPE = "wknd/components/content-panel-list";
	
	@ScriptVariable
	Resource resource;
	
	@Self
	SlingHttpServletRequest request;
	
	@NotNull
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	private int panelNumber;
	
	private List<String> contentPanelList;
	
	@PostConstruct
	public void init() {
		contentPanelList = CommonUlti.getContentPanelList(resource, panelNumber, request);
	}
	
	@Override
	public List<String> getListContentPanel() {
		return contentPanelList;
	}

}
