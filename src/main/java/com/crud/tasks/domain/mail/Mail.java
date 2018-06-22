package com.crud.tasks.domain.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Mail {

    private String mailTo;
    private String subject;
    private String message;
    private List<String> toCC;

    public Mail(String mailTo, String subject, String message) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.message = message;
        toCC = new ArrayList<>();
    }
}
