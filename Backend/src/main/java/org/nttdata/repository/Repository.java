package org.nttdata.repository;

import java.util.List;

public interface Repository<T>{
    List<T> findAll();
}
