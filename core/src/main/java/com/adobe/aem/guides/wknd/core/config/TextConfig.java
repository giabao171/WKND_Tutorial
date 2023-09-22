package com.adobe.aem.guides.wknd.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "text caconfig", description = "text Ca config")
public @interface TextConfig {

	@Property(label = "CaConfig texting", description = "CaConfig testing.")
	String caConfigTexting();
}
