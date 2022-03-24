package com.niit.ArchiveTasks.service;

import com.niit.ArchiveTasks.model.DbSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class SequenceGeneratorService {
    @Autowired
    public MongoOperations mongoOperations;

    public int getSequenceNumber(String sequenceName){
        Query query=new Query(Criteria.where("id").is(sequenceName));// get sequence no, do the filter where id=sequenceName;
        Update update=new Update().inc("seq",1);// update the sequence no
        DbSequence counter=mongoOperations.findAndModify(query,update,options().returnNew(true).upsert(true),DbSequence.class);//modify in the documnet
        return !Objects.isNull(counter)?counter.getSeq():1;// if not null then return counter
    }
}