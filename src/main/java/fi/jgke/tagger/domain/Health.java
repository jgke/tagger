/*
 * Copyright 2016 Jaakko Hannikainen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package fi.jgke.tagger.domain;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Health extends ResourceSupport {

    private final String version;
    private final boolean healthy;

    @JsonCreator
    public Health(@JsonProperty("version") String version, @JsonProperty("healthy") boolean healthy) {
        this.version = version;
        this.healthy = healthy;
    }

    public String getVersion() {
        return version;
    }

    public boolean isHealthy() {
        return healthy;
    }
}
