/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.launcher.daemon.client;

import org.gradle.api.specs.Spec;
import org.gradle.api.specs.Specs;
import org.gradle.initialization.BuildClientMetaData;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.launcher.daemon.configuration.DaemonParameters;
import org.gradle.launcher.daemon.context.DaemonContext;
import org.gradle.logging.internal.OutputEventListener;

import java.io.InputStream;

public class StopDaemonClientServices extends DaemonClientServices {
    public StopDaemonClientServices(ServiceRegistry loggingServices, DaemonParameters daemonParameters, InputStream buildStandardInput) {
        super(loggingServices, daemonParameters, buildStandardInput);
    }

    @Override
    protected DaemonClient createDaemonClient() {
        Spec<DaemonContext> matchAll = Specs.satisfyAll();
        return new DaemonClient(get(DaemonConnector.class), get(BuildClientMetaData.class), get(OutputEventListener.class), matchAll, getBuildStandardInput());
    }
}
