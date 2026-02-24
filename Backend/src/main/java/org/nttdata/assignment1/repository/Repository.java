package org.nttdata.assignment1.repository;

import java.util.List;

public interface Repository<T>{
    List<T> findAll();
}
