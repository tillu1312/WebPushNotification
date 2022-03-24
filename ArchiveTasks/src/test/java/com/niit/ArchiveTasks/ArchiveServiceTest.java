package com.niit.ArchiveTasks;


import com.niit.ArchiveTasks.exceptions.ArchiveAlreadyExistsException;
import com.niit.ArchiveTasks.exceptions.ArchiveNotFoundException;
import com.niit.ArchiveTasks.exceptions.TaskAlreadyExistsException;
import com.niit.ArchiveTasks.model.Archive;
import com.niit.ArchiveTasks.model.BasedOnPriority;
import com.niit.ArchiveTasks.model.Task;
import com.niit.ArchiveTasks.respository.ArchiveRespository;
import com.niit.ArchiveTasks.service.ArchiveService;
import com.niit.ArchiveTasks.service.ArchiveServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArchiveServiceTest {

    @Mock
    private ArchiveRespository archiveRespository;

    @InjectMocks
    private ArchiveServiceImpl archiveService;

    private Archive archive, archive2, archive1, archive3;
    private Task task;
    private Task task2, task3;
    private List<Task> taskList1, taskList2, taskList3;
    private List<Archive> listOfArchive;

    @BeforeEach
    public void setUp() {
        java.util.Date dt = convertionOfdate("2022-03-21");

        task = new Task(1, "testing", "active", "doing positive and negative test cases", BasedOnPriority.LOW, dt);
        task2 = new Task(2, "testing2", "active", "doing positive and negative test cases", BasedOnPriority.MEDIUM, dt);
        task3 = new Task(3, "testing3", "notActive", "doing positive and negative test cases", BasedOnPriority.HIGH, dt);
        taskList1 = new ArrayList<>(Arrays.asList(task));
        taskList2 = new ArrayList<>(Arrays.asList(task, task2));
        taskList3 = new ArrayList<>(Arrays.asList(task, task2, task3));
        archive = new Archive(1, "saisandeep@gmail.com", null);
        archive1 = new Archive(1, "saisandeep@gmail.com", taskList1);
        archive2 = new Archive(1, "saisandeep@gmail.com", taskList2);
        archive3 = new Archive(1, "saisandeep@gmail.com", taskList3);
        listOfArchive = new ArrayList<>(Arrays.asList(archive, archive1));
    }

    @AfterEach
    public void tearDown() {
        archive = null;
        archive2 = null;
        taskList1 = null;
        taskList2 = null;
        task = null;
        task2 = null;
    }

    @Test
    public void saveArchivePositiveTestCase() throws ArchiveAlreadyExistsException {
        when(archiveRespository.findById(archive.getArchiveId())).thenReturn(Optional.ofNullable(null));
        when(archiveRespository.save(archive)).thenReturn(archive);
        assertEquals(archive, archiveService.saveArchive(archive));
        verify(archiveRespository, times(1)).findById(archive.getArchiveId());
        verify(archiveRespository, times(1)).save(archive);
    }

    @Test
    public void saveArchiveNegativeTestCase() {
        when(archiveRespository.findById(archive.getArchiveId())).thenReturn(Optional.ofNullable(archive));
        assertThrows(ArchiveAlreadyExistsException.class, () -> archiveService.saveArchive(archive));
        verify(archiveRespository, times(1)).findById(archive.getArchiveId());
        verify(archiveRespository, times(0)).save(archive);
    }

    @Test
    public void saveTaskToArchivePositiveTestCase() throws ArchiveNotFoundException, TaskAlreadyExistsException {
        when(archiveRespository.findByUserEmail("saisandeep@gmail.com")).thenReturn(archive);
        when(archiveRespository.save(archive)).thenReturn(archive);
        assertEquals(archive1, archiveService.saveTaskToArchive(archive.getUserEmail(), task));
        verify(archiveRespository, times(2)).findByUserEmail("saisandeep@gmail.com");
        verify(archiveRespository, times(1)).save(archive);
    }

    @Test
    public void saveTaskToArchiveNegativeTestCase() {
        when(archiveRespository.findByUserEmail("saisandeep@gmail.com")).thenReturn(null);
        assertThrows(ArchiveNotFoundException.class, () -> archiveService.saveTaskToArchive(archive1.getUserEmail(), task));
        when(archiveRespository.findByUserEmail("saisandeep@gmail.com")).thenReturn(archive1);
        assertThrows(TaskAlreadyExistsException.class, () -> archiveService.saveTaskToArchive(archive1.getUserEmail(), task));
        verify(archiveRespository, times(3)).findByUserEmail("saisandeep@gmail.com");
        verify(archiveRespository, times(0)).save(archive);
    }

    @Test
    public void getAllArchivePositiveTestCase() throws Exception {
        when(archiveRespository.findAll()).thenReturn(listOfArchive);
        assertEquals(listOfArchive, archiveService.getAllArchives());
        verify(archiveRespository, times(1)).findAll();
    }

    @Test
    public void getAllArchiveNegativeTestCase() throws Exception {
        when(archiveRespository.findAll()).thenReturn(null);
        assertEquals(null, archiveService.getAllArchives());
        verify(archiveRespository, times(1)).findAll();
    }

    @Test
    public void getArchiveByUserEmailPositiveTestCase() throws ArchiveNotFoundException {
        when(archiveRespository.findByUserEmail("saisandeep@gmail.com")).thenReturn(archive1);
        assertEquals(taskList1, archiveService.getArchiveByUserEmail("saisandeep@gmail.com"));
        verify(archiveRespository, times(2)).findByUserEmail("saisandeep@gmail.com");

    }

    @Test
    public void getArchiveByUserEmailNegativeTestCase() {
        when(archiveRespository.findByUserEmail("saisandeep@gmail.com")).thenReturn(null);
        assertThrows(ArchiveNotFoundException.class, () -> archiveService.getArchiveByUserEmail("saisandeep@gmail.com"));
        verify(archiveRespository, times(1)).findByUserEmail("saisandeep@gmail.com");
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
