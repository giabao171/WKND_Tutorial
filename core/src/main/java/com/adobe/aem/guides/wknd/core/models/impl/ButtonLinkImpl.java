/**
 * 
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.ButtonLink;


/**
 * 
 */
@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ButtonLink.class},
        resourceType = {ButtonLinkImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ButtonLinkImpl implements ButtonLink{
	
	protected static final String RESOURCE_TYPE = "wknd/components/buttonlink";
	
	@ValueMapValue
	private String title;
	
	@ValueMapValue
	private String link;
	
	@ValueMapValue
	private String linkOuter;

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getLink() {
		// TODO Auto-generated method stub
		return link;
	}

	@Override
	public String getLinkOuter() {
		// TODO Auto-generated method stub
		return linkOuter;
	}

}
