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

import com.adobe.aem.guides.wknd.core.services.SearchService;
import com.google.gson.JsonObject;

@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/wknd/search"}
)
public class SearchServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SearchServlet.class);

    @Reference
    SearchService searchService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        JsonObject searchResult=null;
        try {
            String searchtext = req.getRequestParameter("searchText").getString();
            int pageNumber = Integer.parseInt(req.getRequestParameter("pageNumber").getString())-1;
            int resultPerPage = Integer.parseInt(req.getRequestParameter("resultPerPage").getString());
            int startResult=pageNumber*resultPerPage;
            searchResult=searchService.searchResult(searchtext,startResult,resultPerPage, req.getResourceResolver());
        } catch (Exception e) {
            LOG.error("\n ERROR {} ", e.getMessage());
        }

        resp.setContentType("application/json");
        LOG.error("\n ERROR {} ", searchResult.toString().toString());
        resp.getWriter().write(searchResult.toString());
       
    }

}
