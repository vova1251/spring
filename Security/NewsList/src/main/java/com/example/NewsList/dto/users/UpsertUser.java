package com.example.NewsList.dto.users;

import com.example.NewsList.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpsertUser {

    @NotNull(message = "Поле name должно быть передано")
    private String name;
    @NotNull(message = "Поле email должно быть передано")
    private String email;
    @NotNull(message = "Поле password должно быть передано")
    private String password;
    @NotNull(message = "Поле role должно быть заполнено")
    private RoleType role;

}
