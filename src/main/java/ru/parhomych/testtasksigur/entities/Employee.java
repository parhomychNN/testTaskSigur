package ru.parhomych.testtasksigur.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@DiscriminatorValue("EMPLOYEE")
public class Employee extends Person {

    @Column(name = "HIRE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date hireTime;

    @Column(name = "FIRED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date firedTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    protected Department department;

    public Employee(int id) {
        this.id = id;
    }

    public Employee() {

    }
}
