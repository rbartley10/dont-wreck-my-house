package learn.myhouse.domain;

import learn.myhouse.data.HostRepositoryDouble;
import learn.myhouse.model.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service;

    @BeforeEach
    void setup(){
        service = new HostService(new HostRepositoryDouble());
    }

    @Test
    void shouldFindAll(){
        List<Host> actual = service.findAll();
        assertEquals(2,actual.size());

        assertEquals("536904cf-b7d1-4525-a372-2e8590fae173", actual.get(0).getId());
        assertEquals("maroreus@gmail.com", actual.get(0).getEmail());

        assertEquals("12304cf-b7d1-4525-a372-2e8590fae173", actual.get(1).getId());
        assertEquals("edenhazard@gmail.com", actual.get(1).getEmail());
    }

    @Test
    void shouldFindWithExistingEmail(){
        Host actual = service.findByEmail("edenhazard@gmail.com");

        assertNotNull(actual);
        assertEquals("12304cf-b7d1-4525-a372-2e8590fae173",actual.getId());
        assertEquals("Eden",actual.getFirstName());
        assertEquals("Hazard",actual.getLastName());
        assertEquals("(749) 1378509",actual.getPhone());
        assertEquals("up the road",actual.getAddress());
        assertEquals("our city",actual.getCity());
        assertEquals("NC",actual.getState());
        assertEquals("13491",actual.getPostCode());
        assertEquals(BigDecimal.TEN,actual.getStandardRate());
        assertEquals(new BigDecimal("15"),actual.getWeekendRate());
    }

    @Test
    void shouldNotFindWithNonExistingEmail(){
        Host actual = service.findByEmail("email_does_not_exist");
        assertNull(actual);
    }



}