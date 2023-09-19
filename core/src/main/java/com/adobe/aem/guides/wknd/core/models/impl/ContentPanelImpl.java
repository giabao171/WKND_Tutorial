/**
 * 
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import com.adobe.aem.guides.wknd.core.common.utils.CommonUlti;
import com.adobe.aem.guides.wknd.core.models.ContentPanel;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.Nullable;

/**
 * @author USER
 *
 */
@Model(
		adaptables = {SlingHttpServletRequest.class},
		adapters = {ContentPanel.class},
		resourceType = {ContentPanelImpl.RESOURCE_TYPE},
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = "jackson", extensions = "json")
public class ContentPanelImpl implements ContentPanel{
	
	protected static final String RESOURCE_TYPE = "wknd/components/content-panel";
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String title;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String description;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String link;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private boolean isOpenNewTab;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private boolean doNotAutoPlay;
	
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private boolean hideControlBar;
	
	private Image image;
	
	private String linkTo;
	
	@ScriptVariable
	private PageManager pageManager;
	
	@ScriptVariable
	private Resource resource;
	
	@Self
	private SlingHttpServletRequest request;
	
	@OSGiService
	private ModelFactory modelFactory;
	

	@Override
	public Image getImage() {
		
		Resource imageChildRes = resource.getChild("image");
		if(imageChildRes != null && image == null) {
			image  = modelFactory.getModelFromWrappedRequest(request, imageChildRes, Image.class);
		}
		return image;
	}

	@Override
	@Nullable
	public String getTitle() {	
		return title;
	}

	@Override
	@Nullable
	public String getDescription() {		
		return description;
	}

	@Override
	public String getLink() {
		
		if(linkTo == null) {
			Page page = pageManager.getPage(link);
			linkTo = CommonUlti.getLink(request, page);
		}
		else {
			linkTo = link;
		} 			
		return linkTo;
	}

	@Override
	public boolean isOpenNewTab() {		
		return isOpenNewTab;
	}

	@Override
	public boolean isAutoPlay() {		
		return !doNotAutoPlay;
	}

	@Override
	public boolean hideControlBar() {		
		return hideControlBar;
	}

}
