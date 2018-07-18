package com.crud.tasks.controller;

import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.board.TrelloListDto;
import com.crud.tasks.domain.trello.card.CreatedTrelloCardDto;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrelloController.class)
@RunWith(SpringRunner.class)
public class TrelloControllerTestSuite {

    private static final String GET_TRELLO_BOARDS_URL = "/v1/trello/getTrelloBoards";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrelloFacade trelloFacade;

    @Test
    public void shouldFetchEmptyTrelloBoards() throws Exception {

        //Given
        List<TrelloBoardDto> trelloBoards = new ArrayList<>();

        when(trelloFacade.fetchTrelloBoards()).thenReturn(trelloBoards);

        //When & Then
        mockMvc.perform(get(GET_TRELLO_BOARDS_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)) //or isOk()
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFetchTrelloBoards() throws Exception {

        //Given
        List<TrelloListDto> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloListDto("1", "Test List", false));

        List<TrelloBoardDto> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoardDto("Test Task", "1", trelloLists));

        when(trelloFacade.fetchTrelloBoards()).thenReturn(trelloBoards);

        //when & then
        mockMvc.perform(get(GET_TRELLO_BOARDS_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //TrelloBoard fields
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("Test Task")))
                //Trello list fields
                .andExpect(jsonPath("$[0].lists", hasSize(1)))
                .andExpect(jsonPath("$[0].lists[0].id", is("1")))
                .andExpect(jsonPath("$[0].lists[0].name", is("Test List")))
                .andExpect(jsonPath("$[0].lists[0].closed", is(false)));
    }

    @Test
    public void shouldCreateTrelloCard() throws Exception {

        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test", "Test Description", "top", "1");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("Test", "323", "http://test.com", null);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(trelloCardDto);


        when(trelloFacade.createCard(argThat(any(TrelloCardDto.class)))).thenReturn(createdTrelloCardDto);

        //When & then
        mockMvc.perform(post("/v1/trello/createTrelloCard")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(jsonPath("$.id", is("323")))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.shortUrl", is("http://test.com")));
    }
}