package org.nttdata.service;

import org.nttdata.domain.Pet;
import org.nttdata.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private Repository<Pet> petRepository;

    public List<Pet> getPetList() {
        return petRepository.findAll();
    }

    public List<Pet> getPetListByType(String type) {
        List<Pet> petList = petRepository.findAll();
        List<Pet> newList = new ArrayList<>();

        for (Pet pet : petList) {
            if (pet.getType().equals(type)) {
                newList.add(pet);
            }
        }

        return newList;
    }

    public List<Pet> getListSorted(String sort) {
        List<Pet> petList = petRepository.findAll();

        String[] components = sort.split(",");
        boolean desc=false;
        if(components.length > 1){
            if(components[1].equals("desc")) {
                desc = true;
            }
        }
        switch (components[0]) {
            case "realAge":
                if (desc) {
                    return petList.stream().sorted(Comparator.comparing(Pet::getRealAge).reversed()).toList();
                }
                return petList.stream().sorted(Comparator.comparing(Pet::getRealAge)).toList();
            case "humanAge":
                if (desc){
                   return petList.stream().sorted(Comparator.comparing(Pet::getHumanAge).reversed()).toList();
                }
                return petList.stream().sorted(Comparator.comparing(Pet::getHumanAge)).toList();
        }
        return petList;
    }

    public List<Pet> getListSortedAndFiltered(String type, String sort) {
        List<Pet> petList = getListSorted(sort);
        List<Pet> newList = new ArrayList<>();

        for (Pet pet : petList) {
            if (pet.getType().equals(type)) {
                newList.add(pet);
            }
        }

        return newList;
    }
}
