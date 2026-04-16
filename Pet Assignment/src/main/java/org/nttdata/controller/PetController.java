package org.nttdata.controller;

import org.nttdata.dto.PetDto;
import org.nttdata.service.impl.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    @Autowired
    PetService petService;

    @GetMapping("/pets")
    public ResponseEntity<List<PetDto>> getPets(@RequestParam(required = false) String type, @RequestParam(required = false) String sort){
        if(type!=null){
            if(sort!=null){
                return ResponseEntity.ok().body(petService.getListSortedAndFiltered(type,sort));
            }
            return ResponseEntity.ok().body(petService.getPetListByType(type));
        } else {
            if(sort!=null){
                return ResponseEntity.ok().body(petService.getListSorted(sort));
            }
            return ResponseEntity.ok().body(petService.getPetsList());
        }
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id){
        return ResponseEntity.ok().body(petService.getPetById(id));
    }

    @PostMapping("/pets")
    public ResponseEntity<PetDto> savePet(@RequestBody PetDto petDto){
        return ResponseEntity.ok().body(petService.savePet(petDto));
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<PetDto> deletePet(@PathVariable Long id){
        return ResponseEntity.ok().body(petService.deletePet(id));
    }

    @PutMapping("/pets/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto){
        //returns the old pet
        return ResponseEntity.ok().body(petService.updatePet(id, petDto));
    }
}
