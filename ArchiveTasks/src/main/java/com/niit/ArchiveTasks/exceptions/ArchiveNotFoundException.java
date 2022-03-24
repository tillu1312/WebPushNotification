package com.niit.ArchiveTasks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "archive does not exist")
public class ArchiveNotFoundException extends Exception{

}
