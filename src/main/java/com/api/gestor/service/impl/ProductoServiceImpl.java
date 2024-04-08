package com.api.gestor.service.impl;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.ProductoDao;
import com.api.gestor.pojo.Categoria;
import com.api.gestor.pojo.Producto;
import com.api.gestor.security.jwt.JwtFilter;
import com.api.gestor.service.ProductoService;
import com.api.gestor.util.FacturaUtils;
import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductoServiceImpl  implements ProductoService {

    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, false)){
                    productoDao.save(getProductoFromMap(requestMap, false));
                    return FacturaUtils.getResponseEntity("Producto " + requestMap.get("nombre")  +" creado con exito", HttpStatus.CREATED);
                }
                return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getAllProducts() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(productoDao.getAllProducts(), HttpStatus.OK);

            }else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Producto getProductoFromMap(Map<String , String> requestMap, boolean existe){
        Categoria categoria = new Categoria();
        categoria.setId(Integer.parseInt(requestMap.get("categoriaId")));

        Producto producto = new Producto();

        if(existe){
            producto.setId(Integer.parseInt(requestMap.get("id")));
        }else {
            producto.setStatus("true");
        }

        producto.setCategoria(categoria);
        producto.setNombre(requestMap.get("nombre"));
        producto.setDescripcion(requestMap.get("descripcion"));
        producto.setPrecio(Integer.parseInt(requestMap.get("precio")));
        return producto;

    }

    private boolean validateProductMap(Map<String, String > requestMap, boolean existe){

        if(requestMap.containsKey("nombre")){
            if (requestMap.containsKey("id") && existe){
                return true;
            }
            if (!existe){
                return true;
            }
        }
        return false;
    }




}
