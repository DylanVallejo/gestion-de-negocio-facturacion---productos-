package com.api.gestor.service.impl;

import com.api.gestor.dao.FacturaDAO;
import com.api.gestor.security.jwt.JwtFilter;
import com.api.gestor.service.FacturaService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Stream;


@Slf4j
@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private FacturaDAO facturaDAO;


    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("dentro del generador de reportes");
        try {
            String fileName;
            return null;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    // creando filas
    private void addRows(PdfPTable pdfPTable, Map<String, Object> data){
        log.info("dentro de addRows");
        pdfPTable.addCell((String) data.get("nombre"));
        pdfPTable.addCell((String) data.get("categoria"));
        pdfPTable.addCell((String) data.get("cantidad"));
        pdfPTable.addCell(Double.toString( (Double) data.get("precio")) );
        pdfPTable.addCell(Double.toString( (Double) data.get("total")) );

    }



    //creando una cabecera para la tabla


    private void addTableHeader(PdfPTable pdfPTable){
        log.info("Dentro del addTableHeader");
        Stream.of("Nombre", " Categoria", "Precio", "Sub Total")
                .forEach(columnTitle->{
//                    se puede ahcer con jasperreport tambien
                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfPCell.setBorderWidth(2);
//                    aqui setea los titulos
                    pdfPCell.setPhrase(new Phrase(columnTitle));
                    pdfPCell.setBackgroundColor(BaseColor.YELLOW);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);

                });
    }



}
