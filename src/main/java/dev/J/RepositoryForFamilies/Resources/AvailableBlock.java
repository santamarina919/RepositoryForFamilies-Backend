package dev.J.RepositoryForFamilies.Resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class AvailableBlock {

    private LocalDateTime start;

    private LocalDateTime end;

    public static AvailableBlock OPEN_AVAILABILITY = new AvailableBlock(
                LocalDateTime.of(-999999999,1,1,0,0),
                LocalDateTime.of(999999999,12,31,23,59)
            );


}
