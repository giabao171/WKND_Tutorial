package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.person;

@Model(adaptables = { SlingHttpServletRequest.class }, adapters = { person.class }, resourceType = {
		PersonImpl.RESOURCE }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class PersonImpl implements person {

	protected static final String RESOURCE = "wknd/components/person";
	
	private static final Logger LOG = LoggerFactory.getLogger(PersonImpl.class);

	@Self
	SlingHttpServletRequest request;

	@PostConstruct
	protected void initModel() {

		String productCfpath = request.getRequestPathInfo().getSelectors()[0];
		if (productCfpath == null)
			LOG.error("\n productCfpath null");
		else
			LOG.error("\n productCfpath {}", productCfpath);
	}
}
