package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.ListAddress;

@Model(adaptables = {SlingHttpServletRequest.class},
		adapters = {ListAddress.class},
		resourceType = {ListAddressImpl.RESOURCE_TYPE},
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

public class ListAddressImpl implements ListAddress{
	
	protected static final String RESOURCE_TYPE = "wknd/components/listaddress";
	
	@Inject
	Resource componentResource;
	
	@ValueMapValue
	private List<String> address;

	@Override
	public List<String> getAddress() {
		// TODO Auto-generated method stub
		if(address != null) {
			return new ArrayList<String>(address);
		}
		else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Map<String, String>> getAddressDetails() {
		// TODO Auto-generated method stub
		List<Map<String, String>> addressDtailMap = new ArrayList<>();
		
		try {
			Resource addressDetail = componentResource.getChild("addressdetails");
			if(addressDetail != null) {
				for(Resource detail: addressDetail.getChildren()) {
					Map<String, String> ds = new HashMap<>();
					ds.put("country", detail.getValueMap().get("country", String.class));
					ds.put("city", detail.getValueMap().get("city", String.class));
					addressDtailMap.add(ds);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return addressDtailMap;
	}

}
