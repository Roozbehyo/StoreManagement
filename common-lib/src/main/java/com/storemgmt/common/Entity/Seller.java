package com.storemgmt.common.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
