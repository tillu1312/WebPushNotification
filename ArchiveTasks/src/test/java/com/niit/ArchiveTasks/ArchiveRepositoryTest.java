package com.niit.ArchiveTasks;

import com.niit.ArchiveTasks.model.Archive;
import com.niit.ArchiveTasks.model.BasedOnPriority;
import com.niit.ArchiveTasks.model.Task;
import com.niit.ArchiveTasks.respository.ArchiveRespository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class ArchiveRepositoryTest {
    @Autowired
    private ArchiveRespository archiveRespository;
    private Archive archive;
    private Task task;
    private Task task2;
    private List<Task> taskList;
    @BeforeEach
    public void setUp() {
        java.util.Date dt = convertionOfdate("2022-03-21");
        archive = new Archive(1,"saisandeep@gmail.com",taskList);
        task = new Task(1,"testing","active","doing positive and negative test cases",BasedOnPriority.LOW,dt);
        task2 =new Task(2,"testing2","active","doing positive and negative test cases",BasedOnPriority.MEDIUM,dt);
        taskList=new ArrayList<>(Arrays.asList(task, task2));
    }

    @AfterEach
    public void tearDown() {
        archive = null;
        task = null;
        archiveRespository.deleteAll();

    }

    @Test
    public void saveArchive(){
        archiveRespository.insert(archive);
        Archive archive1=archiveRespository.findById(archive.getArchiveId()).get();
        assertNotNull(archive1);
        assertEquals(archive.getArchiveId(),archive1.getArchiveId());
    }

    @Test
    public void deleteArchive(){
        archiveRespository.insert(archive);
        Archive archive1=archiveRespository.findById(archive.getArchiveId()).get();
        archiveRespository.delete(archive1);
        assertEquals(Optional.empty(),archiveRespository.findById(archive.getArchiveId()));
    }

    @Test
    public  void getAllArchive(){

        archiveRespository.insert(archive);
        List<Archive> list=archiveRespository.findAll();
        assertEquals(1,list.size());
        assertEquals(1,list.get(0).getArchiveId());
    }

    @Test
    public void updateArchive(){
        archiveRespository.insert(archive);
        archive.setUserEmail("niit@gmail.com");
        assertEquals("niit@gmail.com",archive.getUserEmail());
    }

    @Test
    public void findByUserEmailTestCase(){
        archiveRespository.insert(archive);
        Archive archive1=archiveRespository.findByUserEmail(archive.getUserEmail());
        assertNotNull(archive1);
        assertEquals(archive.getArchiveId(),archive1.getArchiveId());
    }
    public Date convertionOfdate(String date) {
        java.util.Date dt = null;
        try {
            dt = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            System.out.println(e);
        }
        return dt;
    }
}
