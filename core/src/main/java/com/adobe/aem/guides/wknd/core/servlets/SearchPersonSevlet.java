package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.SearchPerson;
import com.google.gson.JsonObject;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/searchPerson" })
public class SearchPersonSevlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(SearchServlet.class);

	@Reference
	SearchPerson searchPerson;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {

		JsonObject searchResult = new JsonObject();
		String searchtext = "default";
		int pageNumber = 1;
		int resultPerPage = 1;
		int startResult = 1;
		String id = "default";
		try {
			if(req.getRequestParameter("keywork") != null)
				searchtext = req.getRequestParameter("keywork").getString();
			if(req.getRequestParameter("pageNumber") != null)
				pageNumber = Integer.parseInt(req.getRequestParameter("pageNumber").getString()) - 1;
			if(req.getRequestParameter("resultPerPage") != null)
				resultPerPage = Integer.parseInt(req.getRequestParameter("resultPerPage").getString());
			startResult = pageNumber * resultPerPage;
			
			LOG.error("\n id out IF {} ", id);
			if(req.getRequestParameter("idPerson") != null) {
				id = req.getRequestParameter("idPerson").getString();
				LOG.error("\n id in IF {} ", id);
				searchResult = searchPerson.getPersonDetail(id, req.getResourceResolver());
			}
			else
				searchResult = searchPerson.searchPersonressult(searchtext, startResult, resultPerPage, pageNumber + 1, req.getResourceResolver());
		} catch (Exception e) {
			LOG.error("\n ERROR {} ", e.getMessage());
		}

		resp.setContentType("application/json");
		LOG.error("\n ERROR {} ", searchResult.toString());
		resp.getWriter().write(searchResult.toString());
	}

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject();
		String name = "default";
		String sex = "default";
		String birthday = "default";
		try {
			if(req.getRequestParameter("name") != null)
				name = req.getRequestParameter("name").getString();
			if(req.getRequestParameter("sex") != null)
				sex = req.getRequestParameter("sex").getString();
			if(req.getRequestParameter("birthday") != null)
				birthday = req.getRequestParameter("birthday").getString();

			LOG.error("\n name {} ", name);
			LOG.error("\n sex {} ", sex);
			LOG.error("\n birthday {} ", birthday);

			jsonObject = searchPerson.addPerson(name, sex, birthday, req.getResourceResolver());
		} catch (Exception e) {
			LOG.error("\n Post ERROR {} ", e.getMessage());
		}
		res.setContentType("application/json");
		LOG.error("\n ERROR {} ", "Post Success");
		res.getWriter().write(jsonObject.toString());
	}
	
	@Override
	protected void doDelete(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException {
		JsonObject jsonObject = new JsonObject();
		String idPerson = "default";
		try {
			if(req.getRequestParameter("id") != null)
				idPerson = req.getRequestParameter("id").getString();
			LOG.error("\n Id of Person {} ", idPerson);
			
			jsonObject = searchPerson.deletePerson(idPerson, req.getResourceResolver());
			LOG.error("\n Delete person success");
		} catch (Exception e) {
			LOG.error("\n Delete ERROR {} ", e.getMessage());
		}
		
		res.setContentType("application/json");
		LOG.error("\n ERROR {} ", "Dlete Success");
		res.getWriter().write(jsonObject.toString());
	}
	
	@Override
	protected void doPut(SlingHttpServletRequest req, SlingHttpServletResponse res)
			throws ServletException, IOException {
		JsonObject jsonObject = new JsonObject();
		String name = "default";
		String sex = "default";
		String birthday = "default";
		String id = "default";
		
		try {
			if (req.getRequestParameter("name") != null)
				name = req.getRequestParameter("name").getString();
			if (req.getRequestParameter("sex") != null)
				sex = req.getRequestParameter("sex").getString();
			if (req.getRequestParameter("birthday") != null)
				birthday = req.getRequestParameter("birthday").getString();
			if (req.getRequestParameter("id") != null)
				id = req.getRequestParameter("id").getString();

			LOG.error("\n Eidt name {} ", name);
			LOG.error("\n Eidt sex {} ", sex);
			LOG.error("\n Eidt birthday {} ", birthday);
			LOG.error("\n Eidt id {} ", id);

			jsonObject = searchPerson.deletePerson(id, name, sex, birthday, req.getResourceResolver());
		} catch (Exception e) {
			LOG.error("\n Put ERROR {} ", e.getMessage());
		}
		res.setContentType("application/json");
		LOG.error("\n ERROR {} ", "Put Success");
		res.getWriter().write(jsonObject.toString());
	}
}
