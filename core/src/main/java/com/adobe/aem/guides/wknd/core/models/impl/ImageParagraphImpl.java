package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

import com.adobe.aem.guides.wknd.core.models.ImageParagraph;
import com.adobe.cq.wcm.core.components.models.Image;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ImageParagraph.class},
        resourceType = {ImageParagraphImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ImageParagraphImpl implements ImageParagraph{
	
	protected static final String RESOURCE_TYPE = "wknd/components/imageparagraph";
	
	@Self
    private SlingHttpServletRequest request;
	
	@OSGiService
	private ModelFactory modelFactory;
	
	@ValueMapValue
	private String title;
	
	@ValueMapValue
	private String author;
	
	@ValueMapValue
	private String paragraph;
	
	private Image image;
	
	@PostConstruct
    private void init() {
        // set the image object
        image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
    }

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return author;
	}
	
	@Override
	public String getParagraph() {
		// TODO Auto-generated method stub
		return paragraph;
	}

	private Image getImage() {
		return image;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		final Image componentImage = getImage();
		if(StringUtils.isBlank(title)) {
			return true;
		}
		else if(StringUtils.isBlank(author)) {
			return true;
		}
		else if(componentImage == null || StringUtils.isBlank(componentImage.getSrc())) {
			return true;
		}
		else {
			return false;
		}
	}

	

}
