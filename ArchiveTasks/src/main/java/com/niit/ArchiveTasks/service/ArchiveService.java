package com.niit.ArchiveTasks.service;

import com.niit.ArchiveTasks.exceptions.ArchiveAlreadyExistsException;
import com.niit.ArchiveTasks.exceptions.ArchiveNotFoundException;
import com.niit.ArchiveTasks.exceptions.TaskAlreadyExistsException;
import com.niit.ArchiveTasks.model.Archive;
import com.niit.ArchiveTasks.model.Task;

import java.util.List;

public interface ArchiveService {
    Archive saveArchive(Archive archive) throws ArchiveAlreadyExistsException;
    Archive saveTaskToArchive(String userEmail , Task task) throws ArchiveNotFoundException, TaskAlreadyExistsException;
    List<Archive> getAllArchives() throws Exception;
    List<Task>    getArchiveByUserEmail(String userEmail)throws ArchiveNotFoundException;
}
