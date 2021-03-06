package com.crud.tasks.mapper;

import com.crud.tasks.domain.trello.board.TrelloBoard;
import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.board.TrelloList;
import com.crud.tasks.domain.trello.board.TrelloListDto;
import com.crud.tasks.domain.trello.card.TrelloCard;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class TrelloMapper {

    public List<TrelloBoard> mapToBoards(final List<TrelloBoardDto> trelloBoardDtos) {
        return trelloBoardDtos.stream()
                .map(trelloBoardDto ->
                        new TrelloBoard(trelloBoardDto.getId(), trelloBoardDto.getName(), mapToList(trelloBoardDto.getLists())))
                .collect(toList());

    }

    public List<TrelloBoardDto> mapToBoardsDto(final List<TrelloBoard> trelloBoards) {
        return trelloBoards.stream()
                .map(trelloBoard ->
                        new TrelloBoardDto(trelloBoard.getName(), trelloBoard.getId(), mapToListDto(trelloBoard.getLists())))
                .collect(toList());
    }

    private List<TrelloList> mapToList(final List<TrelloListDto> trelloListDtos) {
        return trelloListDtos.stream()
                .map(trelloListDto -> new TrelloList(trelloListDto.getId(), trelloListDto.getName(), trelloListDto.isClosed()))
                .collect(toList());
    }

    private List<TrelloListDto> mapToListDto(final List<TrelloList> trelloLists) {
        return trelloLists.stream()
                .map(trelloList -> new TrelloListDto(trelloList.getId(), trelloList.getName(), trelloList.isClosed()))
                .collect(toList());
    }

    public TrelloCardDto mapToCardDto(final TrelloCard trelloCard) {
        return new TrelloCardDto(trelloCard.getName(), trelloCard.getDescription(), trelloCard.getPos(), trelloCard.getListId());
    }

    public TrelloCard mapToCard(final TrelloCardDto trelloCardDto) {
        return new TrelloCard(trelloCardDto.getName(), trelloCardDto.getDescription(), trelloCardDto.getPos(), trelloCardDto.getListId());
    }
}
