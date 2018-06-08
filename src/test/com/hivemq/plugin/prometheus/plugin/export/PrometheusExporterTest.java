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
import com.hivemq.spi.config.SystemInformation;
import com.hivemq.spi.exceptions.UnrecoverableException;
import com.hivemq.spi.services.rest.RESTService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import java.io.FileNotFoundException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PrometheusExporterTest {


    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Mock
    private SystemInformation systemInformation;
    @Mock
    private ConfigurationReader configurationReader;
    @Mock
    private PrometheusPluginConfiguration promPluginConfiguration;
    @Mock
    private MetricRegistry metricRegistry;
    @Mock
    private RESTService restService;

    @Before
    public void init() throws Exception {
        initMocks(this);

    }


    @Test
    public void run_without_exception() throws Exception {
        when(configurationReader.readConfiguration()).thenReturn(promPluginConfiguration);
        PrometheusExporter prometheusExporter = new PrometheusExporter(restService, metricRegistry, configurationReader);
        when(promPluginConfiguration.hostIp()).thenReturn("127.0.0.1");
        when(promPluginConfiguration.metricPath()).thenReturn(("/metrics"));
        when(promPluginConfiguration.port()).thenReturn(1234);
        prometheusExporter.start();
    }

    @Test(expected = UnrecoverableException.class)
    public void test_die_from_missing_file() throws Exception {
        when(configurationReader.readConfiguration()).thenThrow(new FileNotFoundException());

        PrometheusExporter prometheusExporter = new PrometheusExporter(restService, metricRegistry, configurationReader);
        prometheusExporter.start();
    }

    @Test(expected = UnrecoverableException.class)
    public void test_die_from_bad_format_configuration() throws Exception {
        when(configurationReader.readConfiguration()).thenThrow(new InvalidConfigurationException("beispiel"));
        PrometheusExporter prometheusExporter = new PrometheusExporter(restService, metricRegistry, configurationReader);
        prometheusExporter.start();
    }


}