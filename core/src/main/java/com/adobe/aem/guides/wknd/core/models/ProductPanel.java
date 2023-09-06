package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.items.ProductPanelItem;
import com.adobe.cq.wcm.core.components.models.Component;

public interface ProductPanel extends Component{
	
	ProductPanelItem getProductPanelInfo();
}
