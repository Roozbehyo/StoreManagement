package com.storemgmt.common.Entity;

import com.storemgmt.common.Entity.Enum.Sex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@SuperBuilder(toBuilder = true)
public class Customer {
    private int id;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String nationalId;
    private String phoneNumber;
    private Sex sex;
}
