package com.crediya.dao;

import java.util.List;

/** Interfaz genérica DAO (SOLID: DIP + ISP). */
public interface IGenericDAO<T> {
    void guardar(T entidad);
    List<T> listar();
}
