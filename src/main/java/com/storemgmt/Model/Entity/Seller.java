package com.storemgmt.Model.Entity;

import com.storemgmt.Model.Entity.Enum.Sex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class Seller {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
