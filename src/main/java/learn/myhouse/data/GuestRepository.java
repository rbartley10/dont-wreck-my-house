package learn.myhouse.data;

import learn.myhouse.model.Guest;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll();

    Guest findByEmail(String email);
}
