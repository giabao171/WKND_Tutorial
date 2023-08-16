/**
 * 
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;

/**
 * 
 */
@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {Byline.class},
        resourceType = {BylineImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class BylineImpl implements Byline{
	
	protected static final String RESOURCE_TYPE = "wknd/components/byline";
	
	@Self
    private SlingHttpServletRequest request;
	
	@OSGiService
	private ModelFactory modelFactory;
	
	@ValueMapValue
    private String name;
	
	@ValueMapValue
    private List<String> occupations;
	
	private Image image;
	
	@PostConstruct
    private void init() {
        // set the image object
        image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public List<String> getOccupations() {
		// TODO Auto-generated method stub
		if(occupations != null) {
			Collections.sort(occupations);
			return new  ArrayList<String>(occupations);
		}
		else {
			return Collections.emptyList();
		}
	}
	
	private Image getImage() {
	    return image;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		final Image componentImage = getImage();
		
		if(StringUtils.isBlank(name)) {
			return true;
		}
		else if (occupations == null || occupations.isEmpty()) {
			return true;
		}
		else if (componentImage == null || StringUtils.isEmpty(componentImage.getSrc())) {
			return true;
		}
		else {
			return false;
		}
		
	}

}
