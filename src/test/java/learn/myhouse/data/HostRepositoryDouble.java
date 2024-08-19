package learn.myhouse.data;

import learn.myhouse.model.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository{

    private final List<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble(){
        Host host1 = new Host();
        host1.setId("536904cf-b7d1-4525-a372-2e8590fae173");
        host1.setFirstName("Marco");
        host1.setLastName("Reus");
        host1.setEmail("maroreus@gmail.com");
        host1.setPhone("(671) 9378501");
        host1.setAddress("down the road");
        host1.setCity("my city");
        host1.setState("CA");
        host1.setPostCode("83492");
        host1.setStandardRate(new BigDecimal("5"));
        host1.setWeekendRate(BigDecimal.TEN);

        Host host2 = new Host();
        host2.setId("12304cf-b7d1-4525-a372-2e8590fae173");
        host2.setFirstName("Eden");
        host2.setLastName("Hazard");
        host2.setEmail("edenhazard@gmail.com");
        host2.setPhone("(749) 1378509");
        host2.setAddress("up the road");
        host2.setCity("our city");
        host2.setState("NC");
        host2.setPostCode("13491");
        host2.setStandardRate(BigDecimal.TEN);
        host2.setWeekendRate(new BigDecimal("15"));

        hosts.add(host1);
        hosts.add(host2);
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findByEmail(String email) {
        return hosts.stream()
                .filter(i -> i.getEmail().equals(email))
                .findFirst().orElse(null);
    }
}
