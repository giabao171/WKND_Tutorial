package com.adobe.aem.guides.wknd.core.services;

import org.apache.sling.api.resource.ResourceResolver;

import com.google.gson.JsonObject;

public interface SearchService {
    public JsonObject searchResult(String searchText,int startResult,int resultPerPage, int pageNumber, ResourceResolver resourceResolver1);
}
