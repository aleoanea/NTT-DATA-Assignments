package org.nttdata.controller;

import org.nttdata.dto.PetDto;
import org.nttdata.service.impl.PetService;
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
    public List<PetDto> getPets(@RequestParam(required = false) String type, @RequestParam(required = false) String sort){
        if(type!=null){
            if(sort!=null){
                return petService.getListSortedAndFiltered(type,sort);
            }
            return petService.getPetListByType(type);
        } else {
            if(sort!=null){
                return petService.getListSorted(sort);
            }
            return petService.getPetsList();
        }
    }
}
