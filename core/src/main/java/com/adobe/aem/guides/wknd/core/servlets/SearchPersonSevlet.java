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

		JsonObject searchResult = null;
		try {
			String searchtext = req.getRequestParameter("keywork").getString();
			int pageNumber = Integer.parseInt(req.getRequestParameter("pageNumber").getString()) - 1;
			int resultPerPage = Integer.parseInt(req.getRequestParameter("resultPerPage").getString());
			int startResult = pageNumber * resultPerPage;
			searchResult = searchPerson.searchPersonressult(searchtext, startResult, resultPerPage, pageNumber + 1, req.getResourceResolver());
		} catch (Exception e) {
			LOG.error("\n ERROR {} ", e.getMessage());
		}

		resp.setContentType("application/json");
		LOG.error("\n ERROR {} ", searchResult.toString().toString());
		resp.getWriter().write(searchResult.toString());
	}

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject();
		try {
			String name = req.getParameter("name");
			String sex = req.getParameter("sex");
			String birthday = req.getParameter("birthday");

			LOG.error("\n name {} ", name);
			LOG.error("\n sex {} ", sex);
			LOG.error("\n birthday {} ", birthday);

//			jsonObject = searchPerson.addPerson(name, sex, birthday);
		} catch (Exception e) {
			LOG.error("\n Post ERROR {} ", e.getMessage());
		}
		res.setContentType("application/json");
		LOG.error("\n ERROR {} ", "Post Success");
		res.getWriter().write(jsonObject.toString());
	}

}
