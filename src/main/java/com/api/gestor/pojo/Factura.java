package com.api.gestor.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;



@NamedQuery(name="Factura.getFacturas", query = " select f from Factura f order by f.id desc")
@NamedQuery(name="Factura.getFacturasByUserName", query = " select f from Factura f where f.createdBy = :username order by f.id desc")
@NamedQuery(name = "Factura.getFacturaPorId", query = "select f from Factura f where f.id = :id ")


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

    @Column(name ="producto_detalles", columnDefinition = "json") //columnDefinition le indica que sera un tipo json lo que almacenaremos
    private String productoDetalles;

    @Column(name = "created_by")
    private String createdBy;

}
