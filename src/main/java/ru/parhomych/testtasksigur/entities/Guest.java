package ru.parhomych.testtasksigur.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@DiscriminatorValue("GUEST")
public class Guest extends Person {

    @Column(name = "VISIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date visitDate;

    @OneToOne()
    @JoinColumn(name = "emp_id", referencedColumnName = "id")
    protected Employee employeeToGo;

    public Guest() {

    }
}
