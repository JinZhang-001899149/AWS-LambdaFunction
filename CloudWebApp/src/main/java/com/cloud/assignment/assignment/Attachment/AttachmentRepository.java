package com.cloud.assignment.assignment.Attachment;

import com.cloud.assignment.assignment.Note.Note;
import com.cloud.assignment.assignment.webSource.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AttachmentRepository extends CrudRepository<Attachment,Integer>{

//    Note save(Note note);
    List<Attachment> findAllByNote(Note note);

    Attachment findByUrl(String url);

    Attachment findById(String id);

    Attachment save(Attachment attachment);

    void deleteById(String id);
//   // Note findByUserandNoteId(User user, String noteId);
}
