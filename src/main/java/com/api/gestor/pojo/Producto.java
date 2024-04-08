package com.api.gestor.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

//@NamedQuery(name = "Producto.getAllProducts", query = "select new com.api.gestor.wrapper.ProductoWrapper(p.id, p.nombre, p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre) from Producto p where categoria.id ='1' ");
@NamedQuery(name = "Producto.getAllProducts", query = "select  new com.api.gestor.wrapper.ProductoWrapper(p.id, p.nombre,p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre ) from Producto p")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoria_fk", nullable = false)
    private Categoria categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name ="status")
    private String status;

//    porducto deberia tener la columna stock y tambien deberia poder crear un producto con el mismo nombre

}
