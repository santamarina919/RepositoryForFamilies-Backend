package dev.J.RepositoryForFamilies.Lists;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public record ListId(UUID listId, UUID groupId) implements Serializable{

}
