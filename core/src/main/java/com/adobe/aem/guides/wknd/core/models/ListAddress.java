package com.adobe.aem.guides.wknd.core.models;

import java.util.List;
import java.util.Map;

public interface ListAddress {
	public List<String> getAddress();
	public List<Map<String, String>> getAddressDetails();
}
