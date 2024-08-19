package learn.myhouse.domain;

import learn.myhouse.data.GuestRepositoryDouble;
import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService service;

    @BeforeEach
    void setup(){
        service = new GuestService(new GuestRepositoryDouble());
    }

    @Test
    void shouldFindAll(){
        List<Guest> actual = service.findAll();
        assertEquals(2,actual.size());

        assertEquals(1, actual.get(0).getId());
        assertEquals("romaribartley@gmail.com", actual.get(0).getEmail());

        assertEquals(2, actual.get(1).getId());
        assertEquals("usainbolt@gmail.com", actual.get(1).getEmail());
    }

    @Test
    void shouldFindWithExistingEmail(){
        Guest actual = service.findByEmail("romaribartley@gmail.com");

        assertNotNull(actual);
        assertEquals(1,actual.getId());
        assertEquals("Romari",actual.getFirstName());
        assertEquals("Bartley",actual.getLastName());
        assertEquals("(372) 1620946",actual.getPhone());
        assertEquals("GA",actual.getState());
    }

    @Test
    void shouldNotFindWithNonExistingEmail(){
        Guest actual = service.findByEmail("email_does_not_exist");
        assertNull(actual);
    }
}