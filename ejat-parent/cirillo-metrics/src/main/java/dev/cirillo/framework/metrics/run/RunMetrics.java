package dev.cirillo.framework.metrics.run;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.annotations.Component;

import io.ejat.framework.spi.AbstractManager;
import io.ejat.framework.spi.IDynamicStatusStoreService;
import io.ejat.framework.spi.IFramework;
import io.ejat.framework.spi.IMetricsProvider;
import io.ejat.framework.spi.IMetricsServer;
import io.ejat.framework.spi.MetricsServerException;
import io.prometheus.client.Gauge;

@Component(service= {IMetricsProvider.class})
public class RunMetrics implements IMetricsProvider, Runnable {
	private final Log                          logger = LogFactory.getLog(getClass());	
	private IFramework                         framework;
	private IMetricsServer                     metricsServer;
	private IDynamicStatusStoreService         dss;

	private Gauge                              localRuns;
	private Gauge                              automatedRuns;
	private Gauge                              waitRuns;

	@Override
	public boolean initialise(IFramework framework, IMetricsServer metricsServer) throws MetricsServerException {
		this.framework = framework;
		this.metricsServer = metricsServer;
		try {
			this.dss = this.framework.getDynamicStatusStoreService("framework");
		} catch (Exception e) {
			throw new MetricsServerException("Unable to initialise Run Metrics", e);
		}

		this.localRuns = Gauge.build()
				.name("cirillo_runs_local_started_total")
				.help("The number of local runs started")
				.register();

		this.automatedRuns = Gauge.build()
				.name("cirillo_runs_automated_started_total")
				.help("The number of automated runs started")
				.register();

		this.waitRuns = Gauge.build()
				.name("cirillo_runs_made_to_wait_total")
				.help("The number of runs made to wait for resources")
				.register();


		return true;
	}

	@Override
	public void start() {

		this.metricsServer.getScheduledExecutorService().scheduleWithFixedDelay(this, 
				1,
				10, 
				TimeUnit.SECONDS);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void run() {
		logger.info("Run Poll");;

		try {
			//*** Local runs
			String sLocalRun = AbstractManager.nulled(dss.get("metrics.runs.local"));
			if (sLocalRun == null) {
				this.localRuns.set(0.0);
			} else {
				this.localRuns.set(Double.parseDouble(sLocalRun));
			}

			//*** Local runs
			String sAutomatedRun = AbstractManager.nulled(dss.get("metrics.runs.automated"));
			if (sAutomatedRun == null) {
				this.automatedRuns.set(0.0);
			} else {
				this.automatedRuns.set(Double.parseDouble(sAutomatedRun));
			}

			//*** Local runs
			String sWaitRun = AbstractManager.nulled(dss.get("metrics.runs.made.to.wait"));
			if (sWaitRun == null) {
				this.waitRuns.set(0.0);
			} else {
				this.waitRuns.set(Double.parseDouble(sWaitRun));
			}
			
			this.metricsServer.metricsPollSuccessful();
		} catch(Exception e) {
			logger.error("Problem with Runs poll",e);
		}

	}


}