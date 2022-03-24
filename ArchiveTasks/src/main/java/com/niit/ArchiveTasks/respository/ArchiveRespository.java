package com.niit.ArchiveTasks.respository;

import com.niit.ArchiveTasks.model.Archive;
import org.apache.el.stream.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface ArchiveRespository extends MongoRepository<Archive,Integer> {


 Archive findByUserEmail(String userEmail);
}
