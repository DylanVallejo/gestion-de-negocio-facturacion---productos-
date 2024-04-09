package com.api.gestor.service.impl;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.CategoriaDAO;
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
import java.util.Optional;

@Service
public class ProductoServiceImpl  implements ProductoService {

    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private CategoriaDAO categoriaDAO;

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

    @Override
    public ResponseEntity<String> actualizarProducto(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                if(validateProductMap(requestMap, true)){
                    Optional<Producto> productoOPtional = productoDao.findById(Integer.parseInt(requestMap.get("id")));
                    String productoID = requestMap.get("id");
                    if(productoOPtional.isPresent()){
                        Producto producto = getProductoFromMap(requestMap, true);
                        producto.setStatus(productoOPtional.get().getStatus());
                        productoDao.save(producto);
                        return FacturaUtils.getResponseEntity("Producto actualizado con exito. " , HttpStatus.OK);
                    }else {
                        return FacturaUtils.getResponseEntity("El producto con el id:  " + productoID + " no existe.", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
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

/*    si deseo actualizar un producto al estar utilizando las relaciones requiero un dao para acceder a los diferentes valores
      luego tendre que buscar el valor en base y combinar las dos entidades

 */
    @Override
    public ResponseEntity<String> actualizarProductoParcial(ProductoWrapper productoWrapperBody, Integer id) {
        try{
            if (jwtFilter.isAdmin()){
                Optional<Producto> productoOptional = productoDao.findById(id);
                Optional<Categoria> categoriaOptional = categoriaDAO.findById(productoWrapperBody.getCategoria_id());
                if (productoOptional.isPresent()){
                    Producto productoAmodificar = validarCamposParciales (productoOptional.get(),productoWrapperBody );
                    if (categoriaOptional.isPresent()){
                        productoAmodificar.setCategoria(categoriaOptional.get());
                    }
                    productoDao.save(productoAmodificar);
                    return FacturaUtils.getResponseEntity("Producto actualizado mediante put", HttpStatus.OK);
                }else {
                    return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Producto validarCamposParciales(Producto validarProducto, ProductoWrapper productoWrapperBody){
        if (productoWrapperBody.getNombre() != null){
            validarProducto.setNombre(productoWrapperBody.getNombre());
        }
        if (productoWrapperBody.getDescripcion() != null){
            validarProducto.setDescripcion(productoWrapperBody.getDescripcion());
        }
        if (productoWrapperBody.getPrecio() != null){
            validarProducto.setPrecio(productoWrapperBody.getPrecio());
        }

        return validarProducto;

    }


    @Override
    public ResponseEntity<String> eliminarProducto(Integer id) {
        try{
            if (jwtFilter.isAdmin()){
                productoDao.deleteById(id);
                return FacturaUtils.getResponseEntity("Producto con id: " + id +" eliminado.", HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
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
