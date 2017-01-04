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
package fi.jgke.tagger.service;

import fi.jgke.tagger.domain.Type;
import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.SourceRepository;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    PersonService personService;

    @Autowired
    ThumbnailService thumbnailService;

    public Source createSource(String title, String url, Type type) {
        Source source = new Source();
        source.setTitle(title);
        source.setUrl(url);
        source.setSourcetype(type);
        source.setPerson(personService.getAuthenticatedPerson());
        thumbnailService.createThumbnailForSource(source);
        source = sourceRepository.save(source);

        return source;
    }

    public boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
