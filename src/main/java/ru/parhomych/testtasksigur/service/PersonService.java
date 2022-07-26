package ru.parhomych.testtasksigur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parhomych.testtasksigur.entities.Person;
import ru.parhomych.testtasksigur.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public List<Person> getAllPersons() {
        return (List<Person>) personRepository.findAll();
    }

    public Person getPersonByCard(byte[] cardBytes) {
        return personRepository.findPersonByCardEquals(cardBytes);
    }

}
