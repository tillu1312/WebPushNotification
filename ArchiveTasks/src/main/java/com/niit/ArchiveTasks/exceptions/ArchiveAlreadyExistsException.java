package com.niit.ArchiveTasks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,reason = "archive already exists")
public class ArchiveAlreadyExistsException extends Exception{
}
