package com.niit.ArchiveTasks.service;

import com.niit.ArchiveTasks.exceptions.ArchiveAlreadyExistsException;
import com.niit.ArchiveTasks.exceptions.ArchiveNotFoundException;
import com.niit.ArchiveTasks.exceptions.TaskAlreadyExistsException;
import com.niit.ArchiveTasks.model.Archive;
import com.niit.ArchiveTasks.model.Task;
import com.niit.ArchiveTasks.respository.ArchiveRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ArchiveServiceImpl implements  ArchiveService{

  ArchiveRespository archiveRespository;

    @Autowired
    public ArchiveServiceImpl(ArchiveRespository archiveRespository)
    {
        this.archiveRespository = archiveRespository;
    }
    @Override
    public Archive saveArchive(Archive archive) throws ArchiveAlreadyExistsException {
        if(archiveRespository.findById(archive.getArchiveId()).isPresent()){
            throw new ArchiveAlreadyExistsException();
        }
        return archiveRespository.save(archive);
    }

    @Override
    public Archive saveTaskToArchive(String userEmail , Task task) throws ArchiveNotFoundException, TaskAlreadyExistsException {
        if(archiveRespository.findByUserEmail(userEmail)==null){
            throw new ArchiveNotFoundException();
        }
        Archive archive = archiveRespository.findByUserEmail(userEmail);
        List<Task> taskList=archive.getTaskList();
        if(taskList==null)
        {
            archive.setTaskList(Arrays.asList(task));
        }
        else {
            for (Task task1 : taskList) {
                if (task1.getTaskId() == task.getTaskId()) {

                    throw new TaskAlreadyExistsException();
                } else
                    taskList.add(task);
                archive.setTaskList(taskList);
            }
        }


        return archiveRespository.save(archive);
    }

    @Override
    public List<Archive> getAllArchives() throws Exception {
        try {
            return archiveRespository.findAll();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Task> getArchiveByUserEmail(String userEmail) throws ArchiveNotFoundException{
        if(archiveRespository.findByUserEmail(userEmail)==null){
            throw new ArchiveNotFoundException();
        }
        else{
            return archiveRespository.findByUserEmail(userEmail).getTaskList();
        }

    }
}
