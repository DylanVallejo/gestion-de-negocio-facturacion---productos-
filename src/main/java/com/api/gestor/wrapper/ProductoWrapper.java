package com.api.gestor.wrapper;


import com.api.gestor.pojo.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoWrapper {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String status;
    private Integer categoria_id;
    private String categoria_nombre;
    private Date fechaCreacion;

    public ProductoWrapper(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
//        this.descripcion = descripcion;
    }

    public ProductoWrapper(Integer id, String nombre, String descripcion, Integer precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

}
