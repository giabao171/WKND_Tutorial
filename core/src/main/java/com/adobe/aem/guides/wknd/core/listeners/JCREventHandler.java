package com.adobe.aem.guides.wknd.core.listeners;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;


@Component(immediate = true,service= EventListener.class)
public class JCREventHandler implements EventListener{

    private static final Logger log = LoggerFactory.getLogger(JCREventHandler.class);
    private Session session;

    @Reference
    SlingRepository slingRepository;
    

    @Activate
    public void activate() throws Exception {
        try {

            String[] nodetypes={"cq:PageContent"};
            session = slingRepository.loginService(null,null);
            session.getWorkspace().getObservationManager().addEventListener(
                    this,                                 //handler
                    Event.NODE_ADDED | Event.PROPERTY_ADDED,      //int code for event type
                    "/content/wknd/us/en/magazine/text",          //path
                    true,                                        //is Deep?
                    null,                                    //UUIDs filter
                    null,                                   //nodetypes filter
//                    null nodetypes,                                   //nodetypes filter
                    true);

        } catch (RepositoryException e){
            log.info(" \n Error while adding Event Listener : {} ",e.getMessage());
        }
    }

    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()){
                log.info("\nJRC Event  Path : {} ",eventIterator.nextEvent().getPath());
            }
        } catch(Exception e){
            //log.error("\n Error while processing events : {} ",e.getMessage());
        }
    }

}
