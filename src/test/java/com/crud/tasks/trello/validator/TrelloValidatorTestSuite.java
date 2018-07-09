package com.crud.tasks.trello.validator;

import com.crud.tasks.domain.trello.board.TrelloBoard;
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
public class TrelloValidatorTestSuite {

    @Autowired
    private TrelloValidator trelloValidator;

    @Test
    public void shouldValidateTrelloBoards() {

        //Given
        TrelloBoard trelloBoard = new TrelloBoard("1", "board", Collections.emptyList());
        TrelloBoard trelloBoard1 = new TrelloBoard("2", "test", Collections.emptyList());

        //When
        List<TrelloBoard> validatedBoards = trelloValidator.validateTrelloBoards(Arrays.asList(trelloBoard, trelloBoard1));

        //Then
        assertEquals(1, validatedBoards.size());
        assertEquals("1", validatedBoards.get(0).getId());

    }

    @Test
    public void shouldValidateEmptyTrelloBoard() {

        //When
        List<TrelloBoard> validatedBoards = trelloValidator.validateTrelloBoards(Collections.emptyList());

        //then
        assertEquals(0, validatedBoards.size());
    }

}