package com.adobe.aem.guides.wknd.core.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;

public class CommonUlti {

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
}
