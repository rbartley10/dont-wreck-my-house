package learn.myhouse.data;

import learn.myhouse.model.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll();

    Host findByEmail(String email);
}
