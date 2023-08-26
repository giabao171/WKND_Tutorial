package com.adobe.aem.guides.wknd.core.services.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.SearchPerson;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.commons.jcr.JcrUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(immediate = true, service = SearchPerson.class)
public class SearchPersonServiceImpl implements SearchPerson {

	private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Reference
	QueryBuilder queryBuilder;

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Activate
	public void activate() {
		LOG.info("\n ----ACTIVATE SEARCH PERSON METHOD----");
	}

	public Map<String, String> createTextSearchQuery(String keywork, int startResult, int resultPerPage) {

		Map<String, String> queryMap = new HashMap<>();
		queryMap.put("path", "/var/datalist/datas");
		queryMap.put("type", "nt:unstructured");
		queryMap.put("property.operation", "like");
		queryMap.put("property", "name");
		queryMap.put("property.value", "%" + keywork + "%");
		queryMap.put("p.offset", Long.toString(startResult));
		queryMap.put("p.limit", Long.toString(resultPerPage));
		return queryMap;
	}

	@Override
	public JsonObject searchPersonressult(String keywork, int startResult, int resultPerPage, int pageNumber,
			ResourceResolver resourceResolverParam) {

		LOG.info("\n ----SEARCH RESULT--------");
		JsonObject objectResult = new JsonObject();
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			final Session session = resourceResolver.adaptTo(Session.class);
			Query query = queryBuilder.createQuery(
					PredicateGroup.create(createTextSearchQuery(keywork, startResult, resultPerPage)), session);

			SearchResult result = query.getResult();
			int perPageResults = result.getHits().size();
			long totalResults = result.getTotalMatches();
			long startingResult = result.getStartIndex();
			double totalPages = Math.ceil((double) totalResults / (double) resultPerPage);

			objectResult.addProperty("totalResult", perPageResults);
			objectResult.addProperty("numberResultOfPage", perPageResults);
			objectResult.addProperty("totalPage", (int) totalPages);
			objectResult.addProperty("curentPage", pageNumber);

			JsonArray resultArray = new JsonArray();
			Iterator<Node> nodesIterator = result.getNodes();
			Node currentNode;
			while (nodesIterator.hasNext()) {
				currentNode = nodesIterator.next();
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", currentNode.getProperty("id").getString());
				jsonObject.addProperty("name", currentNode.getProperty("name").getString());
				jsonObject.addProperty("sex", currentNode.getProperty("sex").getString());

				SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date isoDate = isoFormat.parse(currentNode.getProperty("birthDay").getString());
				String targetDateStr = targetFormat.format(isoDate);

				jsonObject.addProperty("birthday", targetDateStr);

				resultArray.add(jsonObject);
			}
			objectResult.add("results", resultArray);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("\n ERROR {} log query", e.toString());
		}
		return objectResult;
	}
	
	@Override
	public JsonObject getPersonDetail(String id, ResourceResolver resourceResolverParam) {
		
		LOG.info("\n ----Start grt person detail--------");
		JsonObject objectResult = new JsonObject();
		JsonObject objectResponse = new JsonObject();
		
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			
			Resource resource = resourceResolver.getResource("/var/datalist/datas/person"+id);
			if(resource != null) {
				Node currentNode = resource.adaptTo(Node.class);
				LOG.error("\n Get person detail id :"+currentNode.getProperty("id").getString());
				LOG.error("\n Get person detail name :"+currentNode.getProperty("name").getString());
				LOG.error("\n Get person detail sex :"+currentNode.getProperty("sex").getString());
				LOG.error("\n Get person detail birthday :"+currentNode.getProperty("birthDay").getString());
				
				objectResult.addProperty("id", currentNode.getProperty("id").getString());
				objectResult.addProperty("name", currentNode.getProperty("name").getString());
				objectResult.addProperty("sex", currentNode.getProperty("sex").getString());
				
				SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
				SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date isoDate = isoFormat.parse(currentNode.getProperty("birthDay").getString());
				String targetDateStr = targetFormat.format(isoDate);
				
				objectResult.addProperty("birthday", targetDateStr);
				
				objectResponse.add("result", objectResult);
			}
			
		} catch (Exception e) {
			LOG.error("\n Error get person detail {}", e.toString());
		}
		return objectResponse;
	}

	@Override
	public JsonObject addPerson(String name, String sex, String birthday, ResourceResolver resourceResolverParam) {

		LOG.error("\n ----Start add person--------");
		JsonObject objectResult = new JsonObject();
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			final Session session = resourceResolver.adaptTo(Session.class);
			long idPerson = (new Date()).getTime();
			Resource resource;
			resource = resourceResolver.getResource("/var/datalist/datas");

			if (resource == null) {
				resource = resourceResolver.getResource("/var/datalist");

				if (resource == null) {
					JcrUtil.createPath("/var/datalist", "cq:folder", session);
					session.save();
				}

				JcrUtil.createPath("/var/datalist/datas", "cq:Component", session);
				session.save();
			}

			Node personNode = JcrUtil.createPath("/var/datalist/datas/person" + idPerson, "nt:unstructured", session);
			personNode.setProperty("id", idPerson);
			personNode.setProperty("name", name);
			personNode.setProperty("sex", sex);

			if (birthday.equals("default")) 
				birthday = "01-01-2001";
			
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = inputFormat.parse(birthday);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			personNode.setProperty("birthDay", calendar);

			LOG.error("\n ERROR {} Add Person name", name);
			LOG.error("\n ERROR {} Add Person sex", sex);
			LOG.error("\n ERROR {} Add Person name", birthday);

			session.save();
			objectResult.addProperty("status", "successed");
		} catch (Exception e) {
			LOG.error("\n ERROR {} Add Person", e.toString());
		}
		return objectResult;
	}

	@Override
	public JsonObject deletePerson(String id, ResourceResolver resourceResolverParam) {
		
		LOG.error("\n ----Start delete person--------");
		JsonObject objectResult = new JsonObject();
		
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			final Session session = resourceResolver.adaptTo(Session.class);
			
			Resource resource = resourceResolver.getResource("/var/datalist/datas/person"+id);
			if(resource != null) {
				Node currentNode = resource.adaptTo(Node.class);
				currentNode.remove();
				session.save();
			}
			objectResult.addProperty("status", "successed");
		} catch (Exception e) {
			LOG.error("\n ERROR  Delete Person {}", e.toString());
		}
		return objectResult;
	}

	@Override
	public JsonObject deletePerson(String id, String name, String sex, String birthday,
			ResourceResolver resourceResolverParam) {
		
		LOG.error("\n ----Start edit person--------");
		JsonObject objectResult = new JsonObject();
		
		try {
			ResourceResolver resourceResolver = resourceResolverParam;
			final Session session = resourceResolver.adaptTo(Session.class);
			
			Resource resource = resourceResolver.getResource("/var/datalist/datas/person"+id);
			if(resource != null) {
				Node currentNode = resource.adaptTo(Node.class);
				currentNode.setProperty("name", name);
				currentNode.setProperty("sex", sex);
				
				if (birthday.equals("default")) 
					birthday = "01-01-2001";				
				SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = inputFormat.parse(birthday);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				currentNode.setProperty("birthDay", calendar);
				
				LOG.error("\n ERROR {} Edit Person name in serv", name);
				LOG.error("\n ERROR {} Edit Person sex in serv", sex);
				LOG.error("\n ERROR {} Edit Person name in serv", birthday);

				session.save();
				objectResult.addProperty("status", "successed");
			}
		} catch (Exception e) {
			LOG.error("\n ERROR  Edit Person {}", e.toString());
		}
		return objectResult;
	}

}
