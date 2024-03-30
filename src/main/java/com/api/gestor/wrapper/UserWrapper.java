package com.api.gestor.wrapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {

    private Integer id;
    private String nombre;
    private String email;
    private String numero_de_contacto;
    private String status;


}
