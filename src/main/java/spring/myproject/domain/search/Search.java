package spring.myproject.domain.search;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import spring.myproject.domain.gathering.Gathering;

@Entity
public class Search {

    @Id
    private Long id;
    @OneToOne(optional = false)
    private Gathering gathering;
}
