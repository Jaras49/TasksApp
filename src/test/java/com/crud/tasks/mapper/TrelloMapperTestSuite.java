package com.crud.tasks.mapper;

import com.crud.tasks.domain.trello.board.TrelloBoard;
import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.board.TrelloList;
import com.crud.tasks.domain.trello.board.TrelloListDto;
import com.crud.tasks.domain.trello.card.TrelloCard;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TrelloMapperTestSuite {

    @Autowired
    private TrelloMapper trelloMapper;

    @Test
    public void shouldMapToBoards() {

        //Given
        TrelloListDto trelloListDto = new TrelloListDto("22", "testList", true);
        TrelloListDto trelloListDto1 = new TrelloListDto("11", "listTest", false);
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("dto", "22", Arrays.asList(trelloListDto, trelloListDto1));
        TrelloBoardDto trelloBoardDto1 = new TrelloBoardDto("test", "1", Collections.emptyList());

        //When
        List<TrelloBoard> trelloBoards = trelloMapper.mapToBoards(Arrays.asList(trelloBoardDto, trelloBoardDto1));

        //Then
        TrelloBoard trelloBoard1 = trelloBoards.get(0);
        assertEquals("22", trelloBoard1.getId());
        assertEquals("dto", trelloBoard1.getName());
        TrelloList trelloList1 = trelloBoard1.getLists().get(0);
        assertTrue(trelloList1.isClosed());
        assertEquals("22", trelloList1.getId());
        assertEquals("testList", trelloList1.getName());
        TrelloList trelloList2 = trelloBoard1.getLists().get(1);
        assertFalse(trelloList2.isClosed());
        assertEquals("11", trelloList2.getId());
        assertEquals("listTest", trelloList2.getName());

        TrelloBoard trelloBoard2 = trelloBoards.get(1);
        assertEquals("test", trelloBoard2.getName());
        assertEquals("1", trelloBoard2.getId());
        assertArrayEquals(Collections.emptyList().toArray(), trelloBoard2.getLists().toArray());
    }

    @Test
    public void shouldMapToBoardsDto() {

        //Given
        TrelloList trelloList = new TrelloList("2", "list", false);
        TrelloBoard trelloBoard = new TrelloBoard("1", "test", Collections.singletonList(trelloList));

        //When
        List<TrelloBoardDto> trelloBoardDtos = trelloMapper.mapToBoardsDto(Collections.singletonList(trelloBoard));

        //Then
        assertEquals("1", trelloBoardDtos.get(0).getName());
        assertEquals("test", trelloBoardDtos.get(0).getId());
        assertEquals("2", trelloBoardDtos.get(0).getLists().get(0).getId());
        assertEquals("list", trelloBoardDtos.get(0).getLists().get(0).getName());
        assertFalse(trelloBoardDtos.get(0).getLists().get(0).isClosed());
    }

    @Test
    public void shouldMapToCard() {

        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("test", "testDesc", "top", "1");

        //When
        TrelloCard trelloCard = trelloMapper.mapToCard(trelloCardDto);

        //Then
        assertEquals("test", trelloCard.getName());
        assertEquals("testDesc", trelloCard.getDescription());
        assertEquals("top", trelloCard.getPos());
        assertEquals("1", trelloCard.getListId());
    }

    @Test
    public void shouldMapToCardDto() {

        //Given
        TrelloCard trelloCard = new TrelloCard("test", "testDescription", "bottom", "11");

        //When
        TrelloCardDto trelloCardDto = trelloMapper.mapToCardDto(trelloCard);

        //Then
        assertEquals("test", trelloCardDto.getName());
        assertEquals("testDescription", trelloCardDto.getDescription());
        assertEquals("bottom", trelloCardDto.getPos());
        assertEquals("11", trelloCardDto.getListId());
    }
}