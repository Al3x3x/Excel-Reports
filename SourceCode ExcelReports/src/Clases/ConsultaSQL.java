package Clases;

import com.toedter.calendar.JCalendar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Al$x
 */
public class ConsultaSQL {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        // Solicitar al usuario el rango de fechas
        JFrame frame = new JFrame("Rango de fechas");
        JCalendar calendarInicio = new JCalendar();
        JCalendar calendarFin = new JCalendar();
        Object[] options = {"Aceptar", "Cancelar"};
        int result = JOptionPane.showOptionDialog(frame, new Object[]{"Desde: ", calendarInicio, "Hasta: ", calendarFin}, "Seleccione el rango de fechas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.CANCEL_OPTION) {
            return;
        }
        Calendar fechaInicio = calendarInicio.getCalendar();
        Calendar fechaFin = calendarFin.getCalendar();

        // Establecer la conexión JDBC con la base de datos
        Connection conexion = ConexionBD.getConnection();

        String consulta = "SELECT "
                + "ventas.tipo_doc AS DOCUMENTO, "
                + "ventas.caja AS CAJA, "
                + "ventas.no_referen AS NUMERO, "
                + "ventas.f_emision AS FECHA, "
                + "ventas.estado AS ESTATUS, "
                + "ventas.vend AS VENDEDOR, "
                + "partvta.precio * (1 - (partvta.descuento / 100)) AS PRECIO, "
                + "partvta.precio * (1 - (partvta.descuento / 100)) * cantidad AS IMPORTE, "
                + "ventas.Concepto2 AS CONCEPTO, "
                + "partvta.precio * (1 - (partvta.descuento / 100)) * cantidad * (partvta.impuesto / 100) AS IMPUESTO, "
                + "partvta.precio * (1 - (partvta.descuento / 100)) * cantidad * (1 + (partvta.impuesto / 100)) AS TOTAL "
                + "FROM partvta "
                + "INNER JOIN ventas ON partvta.venta = ventas.venta "
                + "WHERE (ventas.estado = 'CO' OR ventas.estado = 'CA') AND "
                + "(ventas.tipo_doc = 'REM' AND ventas.ticket <> 0) OR ventas.tipo_doc = 'DEV' "
                + "ORDER BY ventas.tipo_doc, ventas.no_referen";

        PreparedStatement statement = conexion.prepareStatement(consulta);

        // Ejecutar la consulta y recuperar los resultados
        ResultSet resultSet = statement.executeQuery();
        List<Venta> ventas = new ArrayList<>();
        while (resultSet.next()) {
            Venta venta = new Venta();
            venta.setDocumento(resultSet.getString("DOCUMENTO"));
            venta.setCaja(resultSet.getString("CAJA"));
            venta.setNumero(resultSet.getString("NUMERO"));
            venta.setFecha(resultSet.getDate("FECHA"));
            venta.setEstatus(resultSet.getString("ESTATUS"));
            venta.setVendedor(resultSet.getString("VENDEDOR"));
            venta.setPrecio(resultSet.getDouble("PRECIO"));
            venta.setImporte(resultSet.getDouble("IMPORTE"));
            venta.setConcepto(resultSet.getString("CONCEPTO"));
            venta.setImpuesto(resultSet.getDouble("IMPUESTO"));
            venta.setTotal(resultSet.getDouble("TOTAL"));
            ventas.add(venta);
        }

        // Crear el archivo Excel y las hojas correspondientes
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));
        int resultado = fileChooser.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String nombreArchivo = fileChooser.getSelectedFile().toString();
            if (!nombreArchivo.endsWith(".xlsx")) {
                nombreArchivo += ".xlsx";
            }
            FileOutputStream outputStream = new FileOutputStream(nombreArchivo);
            Workbook workbook = new XSSFWorkbook();
            Sheet hojaActual = null;
            CellStyle estiloHeader = workbook.createCellStyle();
            Font fuenteHeader = workbook.createFont();
            fuenteHeader.setColor(IndexedColors.WHITE.getIndex());
            fuenteHeader.setBold(true);
            estiloHeader.setFillForegroundColor(IndexedColors.BLUE.getIndex());
//        estiloHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
            estiloHeader.setFont(fuenteHeader);
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

            for (Venta venta : ventas) {
                if (venta.getFecha().getTime() >= fechaInicio.getTimeInMillis() && venta.getFecha().getTime() <= fechaFin.getTimeInMillis()) {
                    String fechaString = formatoFecha.format(venta.getFecha());
                    if (hojaActual == null || !hojaActual.getSheetName().equals(fechaString)) {
                        hojaActual = workbook.createSheet(fechaString);
                        Row headerRow = hojaActual.createRow(0);
                        // Crear los encabezados de la hoja
                        Cell cellDocumento = headerRow.createCell(0);
                        cellDocumento.setCellValue("Documento");
                        cellDocumento.setCellStyle(estiloHeader);
                        Cell cellCaja = headerRow.createCell(1);
                        cellCaja.setCellValue("Caja");
                        cellCaja.setCellStyle(estiloHeader);
                        Cell cellNumero = headerRow.createCell(2);
                        cellNumero.setCellValue("Numero");
                        cellNumero.setCellStyle(estiloHeader);
                        Cell cellFecha = headerRow.createCell(3);
                        cellFecha.setCellValue("Fecha");
                        cellFecha.setCellStyle(estiloHeader);
                        Cell cellEstatus = headerRow.createCell(4);
                        cellEstatus.setCellValue("Estatus");
                        cellEstatus.setCellStyle(estiloHeader);
                        Cell cellVendedor = headerRow.createCell(5);
                        cellVendedor.setCellValue("Vendedor");
                        cellVendedor.setCellStyle(estiloHeader);
                        Cell cellPrecio = headerRow.createCell(6);
                        cellPrecio.setCellValue("Precio");
                        cellPrecio.setCellStyle(estiloHeader);
                        Cell cellImporte = headerRow.createCell(7);
                        cellImporte.setCellValue("Importe");
                        cellImporte.setCellStyle(estiloHeader);
                        Cell cellConcepto = headerRow.createCell(8);
                        cellConcepto.setCellValue("Concepto");
                        cellConcepto.setCellStyle(estiloHeader);
                        Cell cellImpuesto = headerRow.createCell(9);
                        cellImpuesto.setCellValue("Impuesto");
                        cellImpuesto.setCellStyle(estiloHeader);
                        Cell cellTotal = headerRow.createCell(10);
                        cellTotal.setCellValue("Total");
                        cellTotal.setCellStyle(estiloHeader);
                    }
                    Row row = hojaActual.createRow(hojaActual.getLastRowNum() + 1);
                    row.createCell(0).setCellValue(venta.getDocumento());
                    row.createCell(1).setCellValue(venta.getCaja());
                    row.createCell(2).setCellValue(venta.getNumero());
                    row.createCell(3).setCellValue(formatoFecha.format(venta.getFecha()));
                    row.createCell(4).setCellValue(venta.getEstatus());
                    row.createCell(5).setCellValue(venta.getVendedor());
                    row.createCell(6).setCellValue(venta.getPrecio());
                    row.createCell(7).setCellValue(venta.getImporte());
                    row.createCell(8).setCellValue(venta.getConcepto());
                    row.createCell(9).setCellValue(venta.getImpuesto());
                    row.createCell(10).setCellValue(venta.getTotal());
                }
            }
            workbook.write(outputStream);
            outputStream.close();
            JOptionPane.showMessageDialog(null, "Archivo generado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se ha seleccionado ninguna ruta de guardado", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
