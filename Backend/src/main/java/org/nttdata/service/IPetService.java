package org.nttdata.service;

import org.nttdata.dto.PetDto;

import java.util.List;

public interface IPetService {
    List<PetDto> getPetsList();
    List<PetDto> getPetListByType(String type);
    List<PetDto> getListSorted(String sort);
    List<PetDto> getListSortedAndFiltered(String type, String sort);
    PetDto getPetById(Long id);
    PetDto savePet(PetDto petDto);
    PetDto deletePet(Long id);
    PetDto updatePet(Long id, PetDto petDto);
}
