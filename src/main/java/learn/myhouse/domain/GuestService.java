package learn.myhouse.domain;

import learn.myhouse.data.GuestRepository;
import learn.myhouse.data.HostRepository;
import learn.myhouse.model.Guest;
import learn.myhouse.model.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {
    private final GuestRepository repository;

    /**
     * constructor that accepts and sets the repository that the
     * service will carry out tasks on
     * @param repository
     */
    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    /**
     * Method that uses the repository to retrieve the current list of guests
     * @return the current list of guests
     */
    public List<Guest> findAll(){
        return repository.findAll();
    }

    /**
     * Method that uses the repository to retrieve a Guest by email
     * @param email
     * @return the Guest with the given email
     */
    public Guest findByEmail(String email){
        return repository.findByEmail(email);
    }
}
