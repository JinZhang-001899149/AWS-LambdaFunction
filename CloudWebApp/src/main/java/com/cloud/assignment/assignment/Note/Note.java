package com.cloud.assignment.assignment.Note;

import com.cloud.assignment.assignment.Attachment.Attachment;
import com.cloud.assignment.assignment.webSource.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "note")
public class Note implements Serializable {

  @Id
  private String noteId;
  private String content;
  private String title;
  private String created_on;
  private String last_updated_on;



  @ManyToOne (fetch = FetchType.LAZY,optional = false)
  @JoinColumn(name = "email", nullable =false)
  @JsonIgnore
  private User user;





    @OneToMany(mappedBy = "note")
  private List<Attachment> attachment;


    public List<Attachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<Attachment> attachment) {
        this.attachment = attachment;
    }







    @com.fasterxml.jackson.annotation.JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getLast_updated_on() {
        return last_updated_on;
    }

    public void setLast_updated_on(String last_updated_on) {
        this.last_updated_on = last_updated_on;
    }



}
