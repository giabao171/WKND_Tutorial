package com.adobe.aem.guides.wknd.core.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.servlets.SearchServlet;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.drew.lang.annotations.NotNull;

public class CommonUlti {

	private static final Logger LOG = LoggerFactory.getLogger(SearchServlet.class);

	@NotNull
	public static String getLink(@NotNull SlingHttpServletRequest request, @NotNull Page page) {

		String redirectTargetpath = page.getProperties().get("cq:redirectTarget", String.class);
		String url = redirectTargetpath;

		if (StringUtils.isEmpty(redirectTargetpath))
			url = page.getPath();

		if (url.startsWith("/content/dam")) {
			url = request.getContextPath() + url;
		} else if (url.startsWith("/content") && !url.endsWith(".html")) {
			url = request.getContextPath() + url + ".html";
		}
		return url;
	}

	public static List<String> getContentPanelList(Resource resourceList, int contentPanelNumber, SlingHttpServletRequest request) {
		List<String> listContentPanel = new ArrayList<String>();
		ResourceResolver resourceReslover = resourceList.getResourceResolver();
		Node contentPanelListNode = resourceList.adaptTo(Node.class);

		if (contentPanelListNode != null) {

			try {
				int i = 0;
				String nodeName;
				Resource contentPanel;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				NodeIterator contentPanelListNodes = contentPanelListNode.getNodes();
				
				Map<String, Object> options = new HashMap<String, Object>();
				options.put("sling:resourceType", "wknd/components/content-panel");
				options.put("jcr:primaryType", "nt:unstructured");

				while (i < contentPanelNumber && contentPanelListNodes.hasNext()) {
					Node contentpanelNodeChild = contentPanelListNodes.nextNode();
					ValueMap childMap = resourceReslover.getResource(contentpanelNodeChild.getPath()).getValueMap();
					if(childMap.get("sling:resourceType", String.class).equals("wknd/components/content-panel")) {
						listContentPanel.add(contentpanelNodeChild.getName());
						i++;
					}else if(childMap.get("sling:resourceType", String.class).equals("wcm/msm/components/ghost")) {
						if(WCMMode.EDIT.equals(WCMMode.fromRequest(request))) {
							listContentPanel.add(contentpanelNodeChild.getName());
						}
					}
				}
				
				try {
					if(WCMMode.EDIT.equals(WCMMode.fromRequest(request))) {
						while(i < contentPanelNumber) {
							nodeName = "content_panel_bao_"+ dateFormat.format(new Date()) + Integer.toString(i);
							contentPanel = resourceReslover.create(resourceList, nodeName, options);
							listContentPanel.add(nodeName);
							i++;
						}
					}
				} catch (Exception e) {
					LOG.error("There is error when create content-panel node{}", e.getMessage());
				}
			} catch (Exception e) {
				LOG.error("There is error {}", e.getMessage());
			}
		}

		return null;
	}
}
