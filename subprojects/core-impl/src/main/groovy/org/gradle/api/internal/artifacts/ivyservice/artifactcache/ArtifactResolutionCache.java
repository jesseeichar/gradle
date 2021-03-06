/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.api.internal.artifacts.ivyservice.artifactcache;

import org.apache.ivy.core.module.id.ArtifactRevisionId;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ModuleVersionRepository;

import java.io.File;

public interface ArtifactResolutionCache {
    File storeArtifactFile(ModuleVersionRepository repository, ArtifactRevisionId artifact, File artifactFile);

    void expireCachedArtifactResolution(ModuleVersionRepository repository, ArtifactRevisionId artifact);

    CachedArtifactResolution getCachedArtifactResolution(ModuleVersionRepository repository, ArtifactRevisionId artifact);

    interface CachedArtifactResolution {
        ArtifactRevisionId getArtifactId();
        
        File getArtifactFile();

        long getAgeMillis();
    }
}
