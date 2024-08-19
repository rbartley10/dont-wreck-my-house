package learn.myhouse.domain;

import learn.myhouse.data.DataException;
import learn.myhouse.data.GuestRepositoryDouble;
import learn.myhouse.data.HostRepositoryDouble;
import learn.myhouse.data.ReservationRepositoryDouble;
import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;

    @BeforeEach
    void setup(){
        service = new ReservationService(
                new ReservationRepositoryDouble(),
                new HostRepositoryDouble(),
                new GuestRepositoryDouble());
    }

    @Test
    void shouldFindReservations(){
        List<Reservation> actual = service.findByHostId("12304cf-b7d1-4525-a372-2e8590fae173");
        assertEquals(3,actual.size());
        assertEquals(LocalDate.of(2024,9,1),actual.get(0).getStartDate());
        assertEquals(LocalDate.of(2024,9,10),actual.get(0).getEndDate());
        assertEquals(LocalDate.of(2024,10,1),actual.get(1).getStartDate());
        assertEquals(LocalDate.of(2024,10,5),actual.get(1).getEndDate());

    }

    @Test
    void shouldAdd() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertTrue(actual.isSuccess());

        assertNotNull(actual.getPayload());

        assertEquals(start,actual.getPayload().getStartDate());
        assertEquals(end,actual.getPayload().getEndDate());
        assertEquals(BigDecimal.TEN,total);
    }

    @Test
    void shouldNotAddWithoutGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = null;

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("host cannot be null",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddWithoutHost() throws DataException {
        Guest guest = null;

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("guest cannot be null",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddWithoutStartDate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = null;
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("start date cannot be null",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddWithoutEndDate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = null;
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("end date cannot be null",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddWithNonExistingGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("noemail@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("guest cannot be found",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddWithNonExistingHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("noemail@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("host cannot be found",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotAddIfStartDateNotBefore() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,9);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("start date must be before end date",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddIfStartDateNotInFuture() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,7,10);
        LocalDate end = LocalDate.of(2024,10,9);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("start date must be in the future",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddIfEndDateOverlap() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,9,30);
        LocalDate end = LocalDate.of(2024,10,3);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddIfStartDateOverlap() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,4);
        LocalDate end = LocalDate.of(2024,10,25);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddIfBothDatesOverlapInside() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,2);
        LocalDate end = LocalDate.of(2024,10,4);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldNotAddIfBothDatesOverlapOutside() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,9,28);
        LocalDate end = LocalDate.of(2024,10,7);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        Result<Reservation> actual = service.add(reservation);
        assertFalse(actual.isSuccess());

        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));

    }

    @Test
    void shouldUpdateExisting() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setFirstName("Eden");
        host.setLastName("Hazard");
        host.setEmail("edenhazard@gmail.com");
        host.setPhone("(749) 1378509");
        host.setAddress("up the road");
        host.setCity("our city");
        host.setState("NC");
        host.setPostCode("13491");
        host.setStandardRate(BigDecimal.TEN);
        host.setWeekendRate(new BigDecimal("15"));

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);
        assertTrue(actual.isSuccess());

    }


    @Test
    void shouldNotUpdateWithoutGuest() throws DataException {
        Guest guest = null;

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("guest cannot be null",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateWithoutHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = null;

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("host cannot be null",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateWithoutStartDate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = null;
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("start date cannot be null",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateWithoutEndDate() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = null;
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("end date cannot be null",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateWithNonExistingGuest() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("doesnotexist@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("guest cannot be found",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateWithNonExistingHost() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("doesnotexist@gmail.com");

        LocalDate start = LocalDate.of(2024,10,10);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("host cannot be found",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateIfStartNotBefore() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,10,16);
        LocalDate end = LocalDate.of(2024,10,15);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("start date must be before end date",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateIfEndOverlap() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,9,27);
        LocalDate end = LocalDate.of(2024,10,5);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateIfStartOverlap() throws DataException {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Romari");
        guest.setLastName("Bartley");
        guest.setEmail("romaribartley@gmail.com");
        guest.setPhone("(372) 1620946");
        guest.setState("GA");

        Host host = new Host();
        host.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host.setEmail("edenhazard@gmail.com");

        LocalDate start = LocalDate.of(2024,10,3);
        LocalDate end = LocalDate.of(2024,10,20);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(1);

        Result<Reservation> actual = service.update(reservation);

        assertFalse(actual.isSuccess());
        assertNull(actual.getPayload());
        assertEquals("date must not overlap with existing reservation",actual.getErrorMessages().get(0));
    }

    @Test
    void shouldDeleteExisting() throws DataException {
        Result<Reservation> actual = service.deleteById(1,"123");
        assertTrue(actual.isSuccess());
    }

    @Test
    void shouldNotDeleteNonExisting() throws DataException {
        Result<Reservation> actual = service.deleteById(999,"123");
        assertFalse(actual.isSuccess());
        assertEquals(1,actual.getErrorMessages().size());
        assertEquals("reservation does not exist", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotDeletePast() throws DataException {
        Result<Reservation> actual = service.deleteById(3,"12304cf-b7d1-4525-a372-2e8590fae173");
        assertFalse(actual.isSuccess());
        assertEquals(1,actual.getErrorMessages().size());
        assertEquals("cannot delete past reservation", actual.getErrorMessages().get(0));
    }

    @Test
    void shouldSort(){
        List<Reservation> actual = service.sortByDate(service.findByHostId("12304cf-b7d1-4525-a372-2e8590fae173"));
        assertEquals(3,actual.size());

        assertEquals(LocalDate.of(2024,1,1),actual.get(0).getStartDate());
        assertEquals(LocalDate.of(2024,1,5),actual.get(0).getEndDate());

        assertEquals(LocalDate.of(2024,9,1),actual.get(1).getStartDate());
        assertEquals(LocalDate.of(2024,9,10),actual.get(1).getEndDate());

        assertEquals(LocalDate.of(2024,10,1),actual.get(2).getStartDate());
        assertEquals(LocalDate.of(2024,10,5),actual.get(2).getEndDate());

    }



}