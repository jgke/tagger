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

import fi.jgke.tagger.exception.TagAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * A single image, URL or such thing.
 *
 * @author jgke
 */
@Entity
@Table(name = "sources")
public class Source extends AbstractPersistable<Long> {

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @OneToMany(mappedBy = "source", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @OneToOne
    @JoinColumn(name = "sourcetype")
    private Type sourcetype;

    @ManyToMany
    @JoinTable(
            name = "tags_sources",
            joinColumns = @JoinColumn(name = "source", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag", referencedColumnName = "id"))
    List<Tag> tags;

    public Source() {
        this.tags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(Type sourcetype) {
        this.sourcetype = sourcetype;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) throws TagAlreadyExistsException {
        if (this.tags.contains(tag)) {
            throw new TagAlreadyExistsException();
        }
        List<Tag> tags = this.getTags();
        tags.add(tag);
        this.setTags(tags);
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
