package org.nttdata.assignment1.controller;

import org.nttdata.assignment1.domain.Pet;
import org.nttdata.assignment1.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetController {

    @Autowired
    PetService petService;

    @GetMapping("/pets")
    public List<Pet> getPets(@RequestParam(required = false) String type, @RequestParam(required = false) String sort){
        if(type!=null){
            if(sort!=null){
                return petService.getListSortedAndFiltered(type,sort);
            }
            return petService.getPetListByType(type);
        } else {
            if(sort!=null){
                return petService.getListSorted(sort);
            }
            return petService.getPetList();
        }
    }
}
