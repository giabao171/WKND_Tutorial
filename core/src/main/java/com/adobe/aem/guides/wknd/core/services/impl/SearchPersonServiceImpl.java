package com.adobe.aem.guides.wknd.core.services.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.factory.ModelFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.SearchPerson;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.commons.date.DateUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(immediate = true, service = SearchPerson.class)
public class SearchPersonServiceImpl implements SearchPerson{
	
	private static final Logger LOG= LoggerFactory.getLogger(SearchServiceImpl.class);
	
	@Reference
	QueryBuilder queryBuilder;
	
	@Reference
    ResourceResolverFactory resourceResolverFactory;
	
	@Activate
    public void activate(){
        LOG.info("\n ----ACTIVATE SEARCH PERSON METHOD----");
    }
	
	public Map<String,String> createTextSearchQuery(String keywork,int startResult,int resultPerPage){
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("path","/var/datalist/datas");
        queryMap.put("type","nt:unstructured");
        queryMap.put("property.operation","like");
        queryMap.put("property","name");
        queryMap.put("property.value","%"+keywork+"%");
        queryMap.put("p.offset",Long.toString(startResult));
        queryMap.put("p.limit",Long.toString(resultPerPage));
        return queryMap;
    }

	@Override
	public JsonObject searchPersonressult(String keywork,int startResult,int resultPerPage, int pageNumber, ResourceResolver resourceResolverParam) {
		LOG.info("\n ----SEARCH RESULT--------");
		JsonObject objectResult = new JsonObject();
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			final Session session = resourceResolver.adaptTo(Session.class);
			Query query = queryBuilder.createQuery(PredicateGroup.create(createTextSearchQuery(keywork, startResult, resultPerPage)), session);
			
			SearchResult result = query.getResult();
			int perPageResults = result.getHits().size();
            long totalResults = result.getTotalMatches();
            long startingResult = result.getStartIndex();
            double totalPages = Math.ceil((double) totalResults / (double) resultPerPage);
            
            objectResult.addProperty("totalResult", perPageResults);
            objectResult.addProperty("numberResultOfPage", perPageResults);
            objectResult.addProperty("totalPage", (int)totalPages);
            objectResult.addProperty("curentPage", pageNumber);
            
            JsonArray resultArray = new JsonArray();
            Iterator<Node> nodesIterator = result.getNodes();
            Node currentNode;
            while(nodesIterator.hasNext()) {
            	currentNode = nodesIterator.next();
            	JsonObject jsonObject = new JsonObject();
            	jsonObject.addProperty("name", currentNode.getProperty("name").getString());
            	
            	SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date isoDate = isoFormat.parse(currentNode.getProperty("birthDay").getString());
                String targetDateStr = targetFormat.format(isoDate);
            	
                jsonObject.addProperty("birthday", targetDateStr);
                
            	jsonObject.addProperty("sex", currentNode.getProperty("sex").getString());
            	resultArray.add(jsonObject);
            }
            objectResult.add("results", resultArray);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("\n ERROR {} log query", e.toString());
		}
		return objectResult;
	}

}
