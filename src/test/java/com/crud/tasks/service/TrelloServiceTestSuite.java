package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.mail.Mail;
import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.card.CreatedTrelloCardDto;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TrelloServiceTestSuite {

    private static final String SUBJECT = "Tasks: new Trello card";

    @InjectMocks
    private TrelloService trelloService;

    @Mock
    private AdminConfig adminConfig;

    @Mock
    private SimpleEmailService simpleEmailService;

    @Mock
    private TrelloClient trelloClient;

    @Test
    public void shouldFetchEmptyList() {

        //Given
        List<TrelloBoardDto> trelloBoardDtos = new ArrayList<>();

        when(trelloClient.getTrelloBoards()).thenReturn(Collections.emptyList());


        //When
        List<TrelloBoardDto> fetchTrelloBoards = trelloService.fetchTrelloBoards();

        //Then
        assertNotNull(fetchTrelloBoards);
        assertEquals(0, trelloBoardDtos.size());
    }

    @Test
    public void shouldFetchTrelloBoards() {

        //Given
        List<TrelloBoardDto> trelloBoardDtos = new ArrayList<>();
        trelloBoardDtos.add(new TrelloBoardDto());

        when(trelloClient.getTrelloBoards()).thenReturn(trelloBoardDtos);

        //When
        List<TrelloBoardDto> fetchTrelloBoards = trelloService.fetchTrelloBoards();

        //Then
        assertNotNull(fetchTrelloBoards);
        assertEquals(1, fetchTrelloBoards.size());
    }

    @Test
    public void shouldCreateTrelloCard() {

        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("test", "desc", "top", "1");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("test", "2", "testUrl", null);

        when(trelloClient.CreateNewCard(trelloCardDto)).thenReturn(createdTrelloCardDto);
        when(adminConfig.getAdminMail()).thenReturn("mail@gmail.com");

        //When
        CreatedTrelloCardDto trelloCard = trelloService.createTrelloCard(trelloCardDto);

        //Then
        verify(simpleEmailService, times(1)).sendMail(new Mail("mail@gmail.com", SUBJECT,
                "New card: " + "test" + " has been created on your Trello account"));

        assertEquals("test", trelloCard.getName());
        assertEquals("2", trelloCard.getId());
        assertEquals("testUrl", trelloCard.getShortUrl());
    }

    @Test
    public void shouldNotCreateTrelloCard() {

        //Given
        when(trelloClient.CreateNewCard(null)).thenReturn(null);

        //When
        CreatedTrelloCardDto trelloCard = trelloService.createTrelloCard(null);

        //then
        assertNull(trelloCard);
        verify(simpleEmailService, times(0)).sendMail(new Mail("", "", ""));
    }
}