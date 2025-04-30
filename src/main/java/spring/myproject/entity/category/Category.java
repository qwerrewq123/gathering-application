package spring.myproject.entity.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.gathering.Gathering;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    public static Category from(String name,Gathering gathering){
        return Category.builder()
                .name(name)
                .gathering(gathering)
                .build();
    }

    public void changeName(String name) {
        this.name = name;
    }
}
