package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.mail.Mail;
import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.card.CreatedTrelloCardDto;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrelloService {

    private static final String SUBJECT = "Tasks: new Trello card";

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    private TrelloClient trelloClient;

    @Autowired
    private SimpleEmailService emailService;

    public List<TrelloBoardDto> fetchTrelloBoards() {
        return trelloClient.getTrelloBoards();
    }

    public CreatedTrelloCardDto createTrelloCard(final TrelloCardDto trelloCardDto) {

        CreatedTrelloCardDto newCard = trelloClient.CreateNewCard(trelloCardDto);

        Optional.ofNullable(newCard).ifPresent(card -> emailService.send(new Mail(adminConfig.getAdminMail(), SUBJECT,
                "New card: " + newCard.getName() + " has been created on your Trello account")));

        return newCard;
    }
}
