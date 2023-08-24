package com.adobe.aem.guides.wknd.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import com.google.gson.JsonObject;

public interface SearchPerson {
	public JsonObject searchPersonressult(String keywork, int startResult ,int resultPerPage, int pageNumber, ResourceResolver resourceResolverParam);
}
