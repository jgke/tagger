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

import fi.jgke.tagger.domain.Source;
import fi.jgke.tagger.repository.SourceRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThumbnailService {

    private static final int THUMBNAIL_SIZE = 128;

    public void createThumbnailForSource(Source source) {
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                Thumbnails.of(new URL(source.getUrl()))
                        .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                        .outputFormat("jpg")
                        .toOutputStream(bos);
                source.setThumbnail(bos.toByteArray());
            }
        } catch (IOException err) {
            System.out.println("Could not create thumbnail: " + err.getMessage());
            source.setThumbnail(null);
        }
    }
}
