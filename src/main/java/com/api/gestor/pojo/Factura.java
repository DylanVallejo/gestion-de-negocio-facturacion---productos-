package com.api.gestor.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "numero_contacto")
    private String numeroContacto;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "total")
    private Integer total;

    @Column(name ="producto_detalles")
    private String productoDetalles;

    @Column(name = "created_by")
    private String createdBy;

}
