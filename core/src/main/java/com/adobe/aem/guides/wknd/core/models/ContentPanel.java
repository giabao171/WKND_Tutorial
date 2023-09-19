/**
 * 
 */
package com.adobe.aem.guides.wknd.core.models;

import java.util.Map;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.Image;

/**
 * @author USER
 *
 */
public interface ContentPanel extends Component{

	Image getImage();
	
	String getTitle();
	
	String getDescription();
	
	String getLink();
	
	boolean isOpenNewTab();
	
	boolean isAutoPlay();
	
	boolean hideControlBar();
}
