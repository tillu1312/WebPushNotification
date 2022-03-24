package com.niit.ArchiveTasks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "task already exists")
public class TaskAlreadyExistsException extends Exception{
}
