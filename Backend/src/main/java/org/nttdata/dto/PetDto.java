package org.nttdata.dto;

public record PetDto(Long id, String name, String owner, String type, String race, Integer realAge, Integer humanAge) {
}
