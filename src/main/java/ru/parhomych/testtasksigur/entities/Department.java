package ru.parhomych.testtasksigur.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Department {

    @Id
    protected int id;
    protected String name;

    public Department(String name) {
        this.name = name;
    }
}
