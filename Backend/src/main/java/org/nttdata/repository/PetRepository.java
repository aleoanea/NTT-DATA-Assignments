package org.nttdata.repository;

import org.nttdata.domain.Pet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class PetRepository implements org.nttdata.repository.Repository<Pet> {

    private List<Pet> generatePetsList() {
        List<Pet> petsList = new ArrayList<>();
        for (int i=0;i<10;i++){
            Pet pet = generateRandomPet();
            petsList.add(pet);
        }
        return petsList;
    }

    private Pet generateRandomPet() {
        Random r = new Random();

        String petName;
        String petOwner = "Owner" + r.nextInt(1000);
        String petType;

        int nrRandom = r.nextInt(1000);
        if (nrRandom % 2 == 0) {
            petType = "cat";
            petName = "Garfield";
        } else {
            petType = "dog";
            petName = "Bobitza";
        }
        petName = petName + r.nextInt(1000);

        String petRace = "Race" + r.nextInt(100);
        int petRealAge = r.nextInt(100);
        int petHumanAge = r.nextInt(20);

        return new Pet(petName,petOwner,petType,petRace,petRealAge,petHumanAge);
    }

    @Override
    public List<Pet> findAll() {
        return generatePetsList();
    }
}
