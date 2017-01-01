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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

@Service
public class ThumbnailService {

    private static final int THUMBNAIL_SIZE = 128;

    public String createThumbnailForImage(String url) {
        String uuid = UUID.randomUUID().toString();
        try {
            Thumbnails.of(new URL(url))
                    .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .outputFormat("jpg")
                    .toFile(uuid + ".jpg");
        } catch (IOException err) {
            return "";
        }
        return uuid;
    }

    public File getThumbnail(String uuid) {
        return new File(uuid + ".jpg");
    }
}
