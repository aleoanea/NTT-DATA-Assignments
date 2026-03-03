package org.nttdata.service.impl;

import jakarta.transaction.Transactional;
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

    private Pet transformFromDto(PetDto petDto){
        return new Pet(petDto.name(),petDto.owner(),petDto.type(),petDto.race(),petDto.realAge(),petDto.humanAge());
    }

    @Override
    public List<PetDto> getPetsList() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(this::transformToDto).collect(Collectors.toList());
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public PetDto getPetById(Long id) {
        var Opet = petRepository.findById(id);
        PetDto petDto = null;
        if(Opet.isPresent()){
            petDto = transformToDto(Opet.get());
        }
        return petDto;
    }

    @Override
    @Transactional
    public PetDto savePet(PetDto petDto) {
        Pet pet = transformFromDto(petDto);
        return transformToDto(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetDto deletePet(Long id) {
        var pet = petRepository.findById(id);
        PetDto petDto=null;
        if(pet.isPresent()){
            petDto=transformToDto(pet.get());
        }
        petRepository.deleteById(id);
        return petDto;
    }

    @Override
    @Transactional
    public PetDto updatePet(Long id, PetDto petDto) {
        var pet = petRepository.findById(id);
        PetDto oldPetDto=null;
        if(pet.isPresent()){
            oldPetDto =transformToDto(pet.get());
        }

        //put means we are not just updating a field, but change the object entirely
        //patch is for updating fields
        petRepository.deleteById(id);
        petRepository.save(transformFromDto(petDto));
        return oldPetDto;
    }

}
