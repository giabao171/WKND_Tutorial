package com.adobe.aem.guides.wknd.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import com.google.gson.JsonObject;

public interface SearchPerson {
	public JsonObject searchPersonressult(String keywork, int startResult ,int resultPerPage, int pageNumber, ResourceResolver resourceResolverParam);
	public JsonObject getPersonDetail(String id, ResourceResolver resourceResolverParam);
	public JsonObject addPerson(String name, String sex, String birthday, ResourceResolver resourceResolverParam);
	public JsonObject deletePerson(String id, ResourceResolver resourceResolverParam);
	public JsonObject deletePerson(String id, String name, String sex, String birthday, ResourceResolver resourceResolverParam);
}
