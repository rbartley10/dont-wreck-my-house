package learn.myhouse.data;

import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    static final String SEED_FILE_PATH = "./data/reservation-seed-2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";

    ReservationFileRepository repo = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void shouldFindAllWithExistingHostId(){
        List<Reservation> all = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertEquals(12,all.size());
        assertEquals(1,all.get(0).getId());
        assertEquals(663,all.get(0).getGuest().getId());
    }

    @Test
    public void shouldNotFindWithNonExistingHostId(){
        List<Reservation> all = repo.findByHostId("id-does-not-exist");
        assertEquals(0,all.size());
    }

    @Test
    public void shouldAdd() throws DataException {
        Host host = new Host();
        host.setId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Guest guest = new Guest();
        guest.setId(10);

        LocalDate start = LocalDate.of(2024,7,1);
        LocalDate end = LocalDate.of(2024,7,3);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);

        Reservation actual = repo.add(reservation);
        List<Reservation> all = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertEquals(start,actual.getStartDate());
        assertEquals(end,actual.getEndDate());
        assertEquals(guest,actual.getGuest());
        assertEquals(host,actual.getHost());
        assertEquals(total,actual.getTotal());

        assertEquals(13,all.size());
    }

    @Test
    public void shouldNotBreakWhenAddNull() throws DataException {
        Reservation actual = repo.add(null);
        assertNull(actual);
    }

    @Test
    public void shouldUpdateExistingId() throws DataException {
        Host host = new Host();
        host.setId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Guest guest = new Guest();
        guest.setId(10);

        LocalDate start = LocalDate.of(2024,7,5);
        LocalDate end = LocalDate.of(2024,7,10);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(12);

        boolean actual = repo.update(reservation);
        List<Reservation> all = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertTrue(actual);
        assertEquals(12,all.size());
        assertEquals(start,all.get(11).getStartDate());
        assertEquals(end,all.get(11).getEndDate());
    }

    @Test
    public void shouldNotUpdateNonExistingId() throws DataException {
        Host host = new Host();
        host.setId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Guest guest = new Guest();
        guest.setId(10);

        LocalDate start = LocalDate.of(2024,7,5);
        LocalDate end = LocalDate.of(2024,7,10);
        BigDecimal total = BigDecimal.TEN;

        Reservation reservation = new Reservation(start,end,guest,host, total);
        reservation.setId(-10101010);

        boolean actual = repo.update(reservation);
        List<Reservation> all = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertFalse(actual);
        assertEquals(12,all.size());
    }

    @Test
    public void shouldDeleteExistingId() throws DataException {
        List<Reservation> allBefore = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        boolean actual = repo.deleteById(11,"2e72f86c-b8fe-4265-b4f1-304dea8762db");
        List<Reservation> allAfter = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertTrue(actual);
        assertEquals(12,allBefore.size());
        assertEquals(11,allAfter.size());
    }

    @Test
    public void shouldNotDeleteNonExistingId() throws DataException {
        List<Reservation> allBefore = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        boolean actual = repo.deleteById(500,"2e72f86c-b8fe-4265-b4f1-304dea8762db");
        List<Reservation> allAfter = repo.findByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");

        assertFalse(actual);
        assertEquals(12,allBefore.size());
        assertEquals(12,allAfter.size());
    }

}