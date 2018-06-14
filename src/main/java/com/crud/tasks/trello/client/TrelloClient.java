package com.crud.tasks.trello.client;

import com.crud.tasks.domain.trello.card.CreatedTrelloCardDto;
import com.crud.tasks.domain.trello.board.TrelloBoardDto;
import com.crud.tasks.domain.trello.card.TrelloCardDto;
import com.crud.tasks.trello.config.TrelloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TrelloClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TrelloConfig trelloConfig;

    public CreatedTrelloCardDto CreateNewCard(TrelloCardDto trelloCardDto) {

        URI url = getCreateNewCardUrl(trelloCardDto);

        return restTemplate.postForObject(url, null, CreatedTrelloCardDto.class);
    }

    public List<TrelloBoardDto> getTrelloBoards() {
        try {
            TrelloBoardDto[] boardResponse = restTemplate.getForObject(getTrelloBoardsUrl(), TrelloBoardDto[].class);

            return Arrays.asList(Optional.ofNullable(boardResponse).orElse(new TrelloBoardDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI getCreateNewCardUrl(TrelloCardDto trelloCardDto) {
        return UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + "/cards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("name", trelloCardDto.getName())
                .queryParam("desc", trelloCardDto.getDescription())
                .queryParam("pos", trelloCardDto.getPos())
                .queryParam("idList", trelloCardDto.getListId()).build().encode().toUri();
    }

    private URI getTrelloBoardsUrl() {
        return UriComponentsBuilder.fromHttpUrl(
                trelloConfig.getTrelloApiEndpoint() + "/members/" + trelloConfig.getTrelloUsername() + "/boards")
                .queryParam("key", trelloConfig.getTrelloAppKey())
                .queryParam("token", trelloConfig.getTrelloToken())
                .queryParam("fields", "name,id")
                .queryParam("lists", "all").build().encode().toUri();
    }
}
