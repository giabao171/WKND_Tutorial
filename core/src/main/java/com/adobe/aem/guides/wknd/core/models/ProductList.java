
package com.adobe.aem.guides.wknd.core.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;

public interface ProductList extends Component{
	
	public List<String> getCfPathsList();
	
	public String[] getCfPaths();
}
