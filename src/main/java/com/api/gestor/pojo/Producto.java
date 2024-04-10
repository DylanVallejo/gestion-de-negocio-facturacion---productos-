package com.api.gestor.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@NamedQuery(name = "Producto.getAllProducts", query = "select  new com.api.gestor.wrapper.ProductoWrapper(p.id, p.nombre,p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre, p.fechaCreacion ) from Producto p")
@NamedQuery(name = "Producto.updateStatus", query = "update Producto p set p.status= :status where p.id= :id")
@NamedQuery(name = "Producto.getProductoPorNombre", query= "select  new com.api.gestor.wrapper.ProductoWrapper(p.id, p.nombre,p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre, p.fechaCreacion ) from Producto p where p.nombre = :nombre ")
@NamedQuery(name = "Producto.getProductosAsc", query = "select new com.api.gestor.wrapper.ProductoWrapper( p.id, p.nombre,p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre, p.fechaCreacion)from Producto p order by p.fechaCreacion ASC ")
@NamedQuery(name = "Producto.getProductosDesc", query = "select new com.api.gestor.wrapper.ProductoWrapper( p.id, p.nombre,p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre, p.fechaCreacion)from Producto p order by p.fechaCreacion DESC ")

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

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

//    producto deberia tener la columna stock y tambien deberia poder crear un producto con el mismo nombre

}
