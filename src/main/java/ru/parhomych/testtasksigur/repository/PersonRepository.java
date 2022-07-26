package ru.parhomych.testtasksigur.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.parhomych.testtasksigur.entities.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findPersonByCardEquals(byte[] cardBytes);

}
