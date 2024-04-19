package com.example.entity;


//import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.SerdeImport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@SerdeImport
@Data
@AllArgsConstructor

public class MessageObject {
    private String command;
    private String content;

}
