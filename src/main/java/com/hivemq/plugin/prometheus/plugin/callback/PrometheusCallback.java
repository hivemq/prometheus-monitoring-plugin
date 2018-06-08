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

package com.hivemq.plugin.prometheus.plugin.callback;

import com.hivemq.plugin.prometheus.plugin.export.PrometheusExporter;
import com.hivemq.spi.callback.events.broker.OnBrokerStart;
import com.hivemq.spi.callback.exception.BrokerUnableToStartException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This callback starts the exporter for the Prometheus metrics when HiveMQ starts.
 *
 * @author Daniel Krüger
 */
public class PrometheusCallback implements OnBrokerStart {


    private static final String START_MSG = "Prometheus-Plugin is starting";
    private static final String MSG_SUCCESS = "Started prometheus plugin successfully";
    private static final Logger log = LoggerFactory.getLogger(PrometheusCallback.class);

    private final PrometheusExporter prometheusExporter;

    @Inject
    public PrometheusCallback(final PrometheusExporter prometheusExporter) {
        this.prometheusExporter = prometheusExporter;
    }

    @Override
    public void onBrokerStart() throws BrokerUnableToStartException {

        log.info(START_MSG);
        prometheusExporter.start();
        log.info(MSG_SUCCESS);
    }


    @Override
    public int priority() {
        return 0;
    }


}
