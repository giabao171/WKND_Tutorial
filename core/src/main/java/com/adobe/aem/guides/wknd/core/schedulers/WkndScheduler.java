/*
 * package com.adobe.aem.guides.wknd.core.schedulers;
 * 
 * 
 * import org.apache.sling.commons.scheduler.ScheduleOptions; import
 * org.apache.sling.commons.scheduler.Scheduler; import
 * org.osgi.service.component.annotations.*; import
 * org.osgi.service.metatype.annotations.Designate; import org.slf4j.Logger;
 * import org.slf4j.LoggerFactory;
 * 
 * import com.adobe.aem.guides.wknd.core.config.SchedulerConfiguration;
 * 
 * @Component(immediate = true, service = Runnable.class)
 * 
 * @Designate(ocd = SchedulerConfiguration.class) public class WkndScheduler
 * implements Runnable { private static final Logger LOG =
 * LoggerFactory.getLogger(WkndScheduler.class);
 * 
 * private int schedulerId;
 * 
 * @Reference private Scheduler scheduler;
 * 
 * @Activate protected void activate(SchedulerConfiguration config) {
 * schedulerId = config.schedulerName().hashCode(); addScheduler(config); }
 * 
 * @Deactivate protected void deactivate(SchedulerConfiguration config) {
 * removeScheduler(); }
 * 
 * protected void removeScheduler() {
 * scheduler.unschedule(String.valueOf(schedulerId)); }
 * 
 * protected void addScheduler(SchedulerConfiguration config) { ScheduleOptions
 * scheduleOptions = scheduler.EXPR(config.cronExpression());
 * scheduleOptions.name(String.valueOf(schedulerId));
 * //scheduleOptions.canRunConcurrently(true); scheduler.schedule(this,
 * scheduleOptions); }
 * 
 * @Override public void run() { LOG.info("\n ====> RUN METHOD"); } }
 */