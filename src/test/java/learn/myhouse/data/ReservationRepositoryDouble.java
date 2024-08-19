package learn.myhouse.data;

import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import learn.myhouse.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository{

    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble(){
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

        Reservation reservation1 = new Reservation(
                LocalDate.of(2024,9,1),
                LocalDate.of(2024,9,10),
                guest,
                host,
                new BigDecimal("100")
        );

        Reservation reservation2 = new Reservation(
                LocalDate.of(2024,10,1),
                LocalDate.of(2024,10,5),
                guest,
                host,
                new BigDecimal("150")
        );

        Reservation reservation3 = new Reservation(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,1,5),
                guest,
                host,
                new BigDecimal("150")
        );
        reservation3.setId(3);

        reservations.add(reservation1);
        reservations.add(reservation2);
        reservations.add(reservation3);
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        return reservations.stream()
                .filter(i -> i.getHost().getId().equals(hostId))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservation.setId(99);
        return reservation;

    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return reservation.getId() > 0;
    }

    @Override
    public boolean deleteById(int id, String hostID) throws DataException {
        return id != 999;
    }

}
