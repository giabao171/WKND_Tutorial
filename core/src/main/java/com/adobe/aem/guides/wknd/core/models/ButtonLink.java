package com.adobe.aem.guides.wknd.core.models;

import com.adobe.cq.wcm.core.components.models.Component;

public interface ButtonLink extends Component{
	
	public String getTitle();
	
	public String getLink();
	
	public String getLinkOuter();
}
