package learn.myhouse.domain;

import learn.myhouse.data.HostRepository;
import learn.myhouse.model.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {
    private final HostRepository repository;

    /**
     * constructor that accepts and sets the repository that the
     * service will carry out tasks on
     * @param repository
     */
    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    /**
     * Method that uses the repository to retrieve the current list of hosts
     * @return the current list of hosts
     */
    public List<Host> findAll(){
        return repository.findAll();
    }

    /**
     * Method that uses the repository to retrieve a Host by email
     * @param email
     * @return the Host with the given email
     */
    public Host findByEmail(String email){
        return repository.findByEmail(email);
    }

}
