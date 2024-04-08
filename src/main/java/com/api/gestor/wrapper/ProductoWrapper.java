package com.api.gestor.wrapper;


import com.api.gestor.pojo.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
