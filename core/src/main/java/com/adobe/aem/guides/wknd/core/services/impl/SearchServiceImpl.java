package com.adobe.aem.guides.wknd.core.services.impl;

import com.adobe.aem.guides.wknd.core.services.SearchService;
import com.adobe.aem.guides.wknd.core.utils.ResolverUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = SearchService.class, immediate = true)
public class SearchServiceImpl implements SearchService{

    private static final Logger LOG= LoggerFactory.getLogger(SearchServiceImpl.class);

    @Reference
    QueryBuilder queryBuilder;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void activate(){
        LOG.info("\n ----ACTIVATE METHOD----");
    }

    public Map<String,String> createTextSearchQuery(String searchText,int startResult,int resultPerPage){
        Map<String,String> queryMap=new HashMap<>();
        queryMap.put("path","/content/dam/wknd");
        queryMap.put("type","dam:Asset");
        queryMap.put("fulltext",searchText);
        queryMap.put("p.offset",Long.toString(startResult));
        queryMap.put("p.limit",Long.toString(resultPerPage));
        return queryMap;
    }

    @Override
    public JsonObject  searchResult(String searchText,int startResult,int resultPerPage, ResourceResolver resourceResolver1){
        LOG.info("\n ----SEARCH RESULT--------");
        JsonObject  searchResult=new JsonObject ();
        try {
//            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
        	ResourceResolver resourceResolver = resourceResolver1;
            final Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(createTextSearchQuery(searchText,startResult,resultPerPage)), session);


            SearchResult result = query.getResult();
            LOG.error("\n ERROR {} query", result.toString());
            

            int perPageResults = result.getHits().size();
            long totalResults = result.getTotalMatches();
            long startingResult = result.getStartIndex();
            double totalPages = Math.ceil((double) totalResults / (double) resultPerPage);
            
            LOG.error("\n ERROR {} ", Integer.valueOf(perPageResults));
            LOG.error("\n ERROR {} ", Long.valueOf(totalResults));
            LOG.error("\n ERROR {} ", Long.valueOf(startingResult));
            LOG.error("\n ERROR {} ", Double.valueOf(totalPages));

            searchResult.addProperty("perpageresult",perPageResults);
            searchResult.addProperty("totalresults",totalResults);
            searchResult.addProperty("startingresult",startingResult);
            searchResult.addProperty("pages",totalPages);


            List<Hit> hits =result.getHits();
            LOG.error("\n List Hist {} ",hits.toString());
            LOG.error("\n List Hist {} ",hits.toString());
            LOG.error("\n List Hist is empty {} ",hits.isEmpty());
            LOG.error("\n List Hist check {} ",hits.toArray().toString());
            JsonArray resultArray=new JsonArray();
            for(Hit hit: hits){
                Page page=hit.getResource().adaptTo(Page.class);
                LOG.error("\n in Page ",000);
                LOG.error("\n in Page Title ",page.getTitle());
                LOG.error("\n in Page Path",page.getPath());

//                JsonObject resultObject=new JsonObject();
//                resultObject.addProperty("title",page.getTitle());
//                resultObject.addProperty("path",page.getPath());
//                resultArray.add(resultObject);
//                LOG.error("\n Page123 {} ",page.getPath());
            	LOG.error("\n in Hit {} ",123);
            }
            searchResult.addProperty("results1",resultArray.toString());
            searchResult.add("results",resultArray);

        }catch (Exception e){
            LOG.info("\n ----ERROR -----{} ",e.getMessage());
        }
        return searchResult;
    }
}
