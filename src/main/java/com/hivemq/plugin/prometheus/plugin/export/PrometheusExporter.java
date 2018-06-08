/*
 * Copyright 2017 dc-square GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hivemq.plugin.prometheus.plugin.export;


import com.codahale.metrics.MetricRegistry;
import com.hivemq.plugin.prometheus.plugin.configuration.ConfigurationReader;
import com.hivemq.plugin.prometheus.plugin.configuration.PrometheusPluginConfiguration;
import com.hivemq.plugin.prometheus.plugin.exception.InvalidConfigurationException;
import com.hivemq.spi.exceptions.UnrecoverableException;
import com.hivemq.spi.services.rest.RESTService;
import com.hivemq.spi.services.rest.listener.HttpListener;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.FileNotFoundException;

/**
 * This class uses the metrics provided by the MetricRegistry of HiveMQ and exports them via the DropwizardExports on
 * The address specified in the configuration file
 *
 * @author Daniel Krüger
 */
public class PrometheusExporter {

    private static final Logger log = LoggerFactory.getLogger(PrometheusExporter.class);

    private final RESTService restService;
    private final MetricRegistry registry;
    private final ConfigurationReader configurationReader;

    /**
     * The parametres are injected via guice.
     *
     * @param restService         the Restservice which handles the servlet
     * @param registry            the MetricRegistry of HiveMQ, which will be exported
     * @param configurationReader the ConfigurationReader, which will read the configuration of the plugin
     */
    @Inject
    PrometheusExporter(final RESTService restService,
                       final MetricRegistry registry,
                       final ConfigurationReader configurationReader) {
        this.restService = restService;
        this.registry = registry;
        this.configurationReader = configurationReader;
    }

    /**
     * This method adds the servlet to the restServer on the ip, port and metric_path specified in the configuration.
     *
     * @throws UnrecoverableException when an error occurs to stop the start of HiveMQ
     */
    public void start() throws UnrecoverableException {
        PrometheusPluginConfiguration prometheusPluginConfiguration = null;
        try {
            prometheusPluginConfiguration = configurationReader.readConfiguration();

        } catch (FileNotFoundException e) {
            log.error("the configuration file: {} could not be read.Shutting down HiveMQ.", e.getMessage());
            throw new UnrecoverableException(false);
        } catch (InvalidConfigurationException e) {
            log.error("{}. Shutting down HiveMQ", e.getMessage());
            throw new UnrecoverableException(false);
        } catch (Exception e) {
            log.error("Unknown error while reading configuration file: {}. Shutting down HiveMQ.", e.getMessage());
            throw new UnrecoverableException(false);
        }
        try {
            restService.addListener(new HttpListener("prometheusExportListener", prometheusPluginConfiguration.hostIp(), prometheusPluginConfiguration.port()));
            CollectorRegistry.defaultRegistry.register(new DropwizardExports(registry));
            restService.addServlet(new MonitoredMetricServlet(CollectorRegistry.defaultRegistry, registry), prometheusPluginConfiguration.metricPath());
        } catch (Exception e) {
            log.error("Error occurred while trying to generate the servlet and register it at the RestService {}. Shutting down HiveMQ", e.getCause().getMessage());
            throw new UnrecoverableException(false);
        }
    }
}
