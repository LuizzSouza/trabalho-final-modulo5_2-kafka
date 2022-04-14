package com.tindev.tindevapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tindev.tindevapi.dto.log.LogDTO;
import com.tindev.tindevapi.enums.TipoLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutorService {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value(value = "${kafka.topic.teste}")
    private String topic;

    public void enviarMensagem(String mensagem) {
        Message<String> message = MessageBuilder.withPayload(mensagem)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, "mensagem")
                .build();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult result) {
                log.info(" Log enviado para o kafka com o texto: {} ", mensagem);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error(" Erro ao publicar duvida no kafka com a mensagem: {}", mensagem, ex);
            }
        });
    }

//    public void enviarRegistro(RegistroCreateDTO registroCreateDTO) throws JsonProcessingException {
//        String payload = objectMapper.writeValueAsString(registroCreateDTO);

//        Message<String> message = MessageBuilder.withPayload(payload)
//                .setHeader(KafkaHeaders.TOPIC, topic)
//                .setHeader(KafkaHeaders.MESSAGE_KEY, UUID.randomUUID().toString())
//                .build();
//
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
//        future.addCallback(new ListenableFutureCallback<>() {

//            @Override
//            public void onSuccess(SendResult result) {
//                log.info(" Log enviado para o kafka com o texto: {} ", message);
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error(" Erro ao publicar duvida no kafka com a mensagem: {}", message, ex);
//            }
//        });
//    }

    public void enviarLog(LogDTO logDTO) throws JsonProcessingException {

        String payload = objectMapper.writeValueAsString(logDTO);

        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.MESSAGE_KEY, "mensagem")
                .build();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(message);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult result) {
                log.info(" Log enviado para o kafka com o texto: {} ");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error(" Erro ao publicar duvida no kafka com a mensagem: {}", ex);
            }
        });
    }

}