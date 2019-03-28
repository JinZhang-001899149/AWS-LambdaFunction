package com.cloud.assignment.assignment.Attachment;

import com.cloud.assignment.assignment.Note.Note;
import com.cloud.assignment.assignment.webSource.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.File;


@Entity
@Table(name = "attachment")
public class Attachment {


    @Id
    private String id;

    private String url;
    //private File file;



    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "noteId", nullable =false)
    @JsonIgnore

    private Note note;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public File getFile() {
//        return file;
//    }
//
//    public void setFile(File file) {
//        this.file = file;
//    }
}
