package dev.J.RepositoryForFamilies.Events;

import java.util.UUID;

public record EventId(UUID eventId, UUID groupId){}
