package com.brfyamada.webflux.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table("Anime")
public class Anime {
    @Id
    private Integer id;
    @NotNull
    @NotEmpty(message = "The name of this cannot be empty")
    private String name;
}
