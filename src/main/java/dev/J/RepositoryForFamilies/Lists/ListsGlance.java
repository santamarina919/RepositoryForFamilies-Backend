package dev.J.RepositoryForFamilies.Lists;

import java.time.LocalDate;
import java.util.UUID;

public interface ListsGlance {

    UUID getListId();

    String getName();

    String getItemDomain();

    LocalDate getExpiration();


}
