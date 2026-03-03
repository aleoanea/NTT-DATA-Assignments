package org.nttdata.service.impl;

import org.nttdata.domain.Pet;
import org.nttdata.dto.PetDto;
import org.nttdata.repository.IPetRepository;
import org.nttdata.service.IPetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService implements IPetService {

    @Autowired
    private IPetRepository petRepository;

    private PetDto transformToDto(Pet pet){
        return new PetDto(pet.getId(), pet.getName(), pet.getOwner(), pet.getType(), pet.getRace(), pet.getRealAge(), pet.getHumanAge());
    }

    public List<PetDto> getPetsList() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(this::transformToDto).collect(Collectors.toList());
    }

    public List<PetDto> getPetListByType(String type) {
        List<Pet> petList = petRepository.findAll();
        List<Pet> newList = new ArrayList<>();

        for (Pet pet : petList) {
            if (pet.getType().equals(type)) {
                newList.add(pet);
            }
        }

        return newList.stream().map(this::transformToDto).collect(Collectors.toList());
    }

    public List<PetDto> getListSorted(String sort) {
        List<Pet> petList = petRepository.findAll();

        String[] components = sort.split(",");
        boolean desc=false;
        if(components.length > 1){
            if(components[1].equals("desc")) {
                desc = true;
            }
        }
        return switch (components[0]) {
            case "realAge" -> {
                if (desc) {
                    yield petList.stream().sorted(Comparator.comparing(Pet::getRealAge).reversed()).map(this::transformToDto).collect(Collectors.toList());
                }
                yield petList.stream().sorted(Comparator.comparing(Pet::getRealAge)).map(this::transformToDto).collect(Collectors.toList());
            }
            case "humanAge" -> {
                if (desc) {
                    yield petList.stream().sorted(Comparator.comparing(Pet::getHumanAge).reversed()).map(this::transformToDto).collect(Collectors.toList());
                }
                yield petList.stream().sorted(Comparator.comparing(Pet::getHumanAge)).map(this::transformToDto).collect(Collectors.toList());
            }
            default -> petList.stream().map(this::transformToDto).collect(Collectors.toList());
        };
    }

    public List<PetDto> getListSortedAndFiltered(String type, String sort) {
        List<PetDto> petList = getListSorted(sort);
        List<PetDto> newList = new ArrayList<>();

        for (PetDto pet : petList) {
            if (pet.type().equals(type)) {
                newList.add(pet);
            }
        }
        return newList;
    }
}
