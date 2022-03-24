package com.niit.ArchiveTasks.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Archive {
    @Transient //this particular filed i don't want to persist to mongodb
    public static final String SEQUENCE_NAME= "user_sequence";//our archiveId is our sequence name

    @Id
    private int archiveId;
    private String userEmail;
    private List<Task> taskList;


}
