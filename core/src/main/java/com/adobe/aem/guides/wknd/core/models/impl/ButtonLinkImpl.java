/**
 * 
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger LOG = LoggerFactory.getLogger(ButtonLinkImpl.class);
	
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
	
	@PostConstruct
	public void initModel() {
		LOG.error("/n Button link init");
	}

}
