package com.brfyamada.webflux.consumer;

import com.brfyamada.webflux.controller.AnimeController;
import com.brfyamada.webflux.domain.Anime;
import com.brfyamada.webflux.repository.AnimeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
@RequiredArgsConstructor
public class SqsConsumer {


    private static final Logger logger = LoggerFactory.getLogger(AnimeController.class);

    private final AnimeRepository animeRepository;

    private final SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.end-point}")
    private String sqsServiceEndpoint;

    @PostConstruct
    public void continuousListener() {
        Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
                sqsAsyncClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .maxNumberOfMessages(5)
                                .queueUrl(sqsServiceEndpoint)
                                .waitTimeSeconds(10)
                                .visibilityTimeout(30)
                                .build()
                )
        );

        receiveMessageResponseMono
                .repeat()
                .retry()
                .map(ReceiveMessageResponse::messages)
                .map(Flux::fromIterable)
                .flatMap(messageFlux -> messageFlux)
                .subscribe(messageToProcess -> {
                    logger.info("Processing batch message of Animes:  " + messageToProcess.body());

                    try {
                        this.saveAnimeFromMessage(messageToProcess).log("#### Consuming Message ####")
                                .doOnError(throwable -> {
                                    logger.error("Saving Anime from message Process error");
                                }).doOnSuccess(it -> logger.info("Anime Success: " + it))
                                .subscribe();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(sqsServiceEndpoint).receiptHandle(messageToProcess.receiptHandle()).build())
                            .thenAccept(deletedMessage -> {
                                logger.info("message with id {}, processed an deleted successfully" + messageToProcess.messageId());
                            });
                });
    }

    private Mono<List<Anime>> saveAnimeFromMessage(Message message) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        logger.info("Parsing Message to Object Anime List {}",message);
        List<Anime> animes = mapper.readValue(message.body(), new TypeReference<List<Anime>>(){});

        logger.info("Object parsed:  {}",animes);

        return Flux.just(animes)
                .flatMap(animeRepository::saveAll)
                .doOnError(throwable -> {
                    logger.error("Execution Error");
                }).doOnComplete( () -> {
                    logger.info("[SqsConsumer] Anime saved: {}",animes);
                })
                .then(Mono.just(animes));
    }

}
