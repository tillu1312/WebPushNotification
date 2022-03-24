package com.niit.ArchiveTasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.ArchiveTasks.controller.ArchiveController;
import com.niit.ArchiveTasks.exceptions.ArchiveAlreadyExistsException;
import com.niit.ArchiveTasks.exceptions.ArchiveNotFoundException;
import com.niit.ArchiveTasks.exceptions.TaskAlreadyExistsException;
import com.niit.ArchiveTasks.model.Archive;
import com.niit.ArchiveTasks.model.BasedOnPriority;
import com.niit.ArchiveTasks.model.Task;
import com.niit.ArchiveTasks.respository.ArchiveRespository;
import com.niit.ArchiveTasks.service.ArchiveServiceImpl;
import com.niit.ArchiveTasks.service.SequenceGeneratorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ArchiveControllerTest {
    @Mock
    private ArchiveServiceImpl archiveService;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @InjectMocks
    private ArchiveController archiveController;

    private Archive archive, archive2, archive1, archive3;
    private Task task;
    private Task task2, task3;
    private List<Task> taskList1, taskList2, taskList3;
    private List<Archive> listOfArchive;
    @Autowired
    private MockMvc mockMvc;
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
        mockMvc = MockMvcBuilders.standaloneSetup(archiveController).build();
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
    public void saveContentDetailsPositiveTest() throws Exception {
        when(archiveService.saveArchive(any())).thenReturn(archive1);
        mockMvc.perform(post("/archiveservice/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive1)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).saveArchive(any());
    }

    @Test
    public void saveContentDetailsNegativeTest() throws Exception {
        when(archiveService.saveArchive(any())).thenThrow(ArchiveAlreadyExistsException.class);
        mockMvc.perform(post("/archiveservice/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive)))
                .andExpect(status().isConflict())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).saveArchive(any());
    }

    @Test
    public void saveTaskToArchivePositiveTest() throws Exception {
        when(archiveService.saveTaskToArchive(anyString(),any())).thenReturn(archive1);
        mockMvc.perform(post("/archiveservice/archive/saisandeep@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive1)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).saveTaskToArchive(anyString(),any());
    }

    @Test
    public void saveTaskToArchiveNegativeTest() throws Exception {
        when(archiveService.saveTaskToArchive(anyString(),any())).thenThrow(ArchiveNotFoundException.class);
        mockMvc.perform(post("/archiveservice/archive/saisandeep@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).saveTaskToArchive(anyString(),any());
    }

    @Test
    public void getAllContentPositiveTest() throws Exception {
        when(archiveService.getAllArchives()).thenReturn(listOfArchive);
        mockMvc.perform(get("/archiveservice/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive1)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).getAllArchives();
    }

    @Test
    public void getAllContentNegativeTest() throws Exception {
        when(archiveService.getAllArchives()).thenThrow(ArchiveNotFoundException.class);
        mockMvc.perform(get("/archiveservice/archive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive)))
                .andExpect(status().is5xxServerError())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).getAllArchives();
    }

    @Test
    public void getAllArchiveByUserEmailPositiveTest() throws Exception {
        when(archiveService.getArchiveByUserEmail(anyString())).thenReturn(taskList1);
        mockMvc.perform(get("/archiveservice/archive/saisandeep@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive1)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).getArchiveByUserEmail(anyString());
    }

    @Test
    public void getAllArchiveByUserEmailNegativeTest() throws Exception {
        when(archiveService.getArchiveByUserEmail(anyString())).thenThrow(ArchiveNotFoundException.class);
        mockMvc.perform(get("/archiveservice/archive/saisandeep@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(archive)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
        verify(archiveService, times(1)).getArchiveByUserEmail(anyString());
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

    private static String jsonToString(final Object obj) throws JsonProcessingException {
        String result = null;
        try {
            ObjectMapper mapper = new ObjectMapper(); // provides functionality for reading and writing JSON, either to and from POJO
            String jsonContent = mapper.writeValueAsString(obj);
            result = jsonContent;
        } catch (JsonProcessingException e) {
            result = "error while conversion";
        }
        return result;
    }
}
