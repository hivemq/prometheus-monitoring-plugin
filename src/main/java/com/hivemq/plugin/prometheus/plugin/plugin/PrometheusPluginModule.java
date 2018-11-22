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

package com.hivemq.plugin.prometheus.plugin.plugin;

import com.hivemq.spi.HiveMQPluginModule;
import com.hivemq.spi.PluginEntryPoint;
import com.hivemq.spi.plugin.meta.Information;


/**
 * This is the plugin module class, which handles the initialization and configuration
 *
 * @author Daniel Krüger
 */
@Information(name = "HiveMQ Prometheus Plugin", author = "dc-square GmbH", version = "1.1.0")
public class PrometheusPluginModule extends HiveMQPluginModule {


    @Override
    protected void configurePlugin() {

    }


    @Override
    protected Class<? extends PluginEntryPoint> entryPointClass() {
        return PrometheusMainClass.class;
    }
}
