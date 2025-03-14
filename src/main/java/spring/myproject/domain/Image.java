package spring.myproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "image")
@Entity
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    Board board;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entire_image_id")
    private Gathering gathering;

    public void changeBoard(Board board){
        this.board = board;
    }
}
