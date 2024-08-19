package learn.myhouse.data;

import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private final List<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble(){
        Guest guest1 = new Guest();
        guest1.setId(1);
        guest1.setFirstName("Romari");
        guest1.setLastName("Bartley");
        guest1.setEmail("romaribartley@gmail.com");
        guest1.setPhone("(372) 1620946");
        guest1.setState("GA");

        Guest guest2 = new Guest();
        guest2.setId(2);
        guest2.setFirstName("Usain");
        guest2.setLastName("Bolt");
        guest2.setEmail("usainbolt@gmail.com");
        guest2.setPhone("(812) 4623976");
        guest2.setState("PA");

        guests.add(guest1);
        guests.add(guest2);
    }

    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findByEmail(String email) {
        return guests.stream()
                .filter(i -> i.getEmail().equals(email))
                .findFirst().orElse(null);
    }
}
