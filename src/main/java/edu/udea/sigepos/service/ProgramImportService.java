package edu.udea.sigepos.service;

import edu.udea.sigepos.model.AcademicUnit;
import edu.udea.sigepos.model.AgreementType;
import edu.udea.sigepos.model.Program;
import edu.udea.sigepos.repository.AcademicUnitRepository;
import edu.udea.sigepos.repository.AgreementTypeRepository;
import edu.udea.sigepos.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProgramImportService {

    private final ProgramRepository programRepository;
    private final AcademicUnitRepository academicUnitRepository;
    private final AgreementTypeRepository agreementTypeRepository;

    public void importExcel(MultipartFile file) throws Exception{
        try(InputStream is = file.getInputStream()) {
            Workbook workbookb = WorkbookFactory.create(is);
            Sheet sheet = workbookb.getSheetAt(1);

            List<Program> programsToSave = new ArrayList<>();

            for(int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Program program = new Program();

                String codigoStr = getCellValueAsString(row.getCell(0));
                Long codigo = (codigoStr != null && !codigoStr.isEmpty()) ? Long.parseLong(codigoStr) : null;

                String nombre = getCellValueAsString(row.getCell(1));
                String unidadAcademicaNombre = getCellValueAsString(row.getCell(2));

                String sniesStr = getCellValueAsString(row.getCell(3));
                Long snies = null;
                if(sniesStr != null && !sniesStr.isEmpty()){
                    if(sniesStr.contains(".")){
                        sniesStr = sniesStr.substring(0,sniesStr.indexOf("."));
                    }
                    snies = Long.parseLong(sniesStr);
                }

                String registroStr = getCellValueAsString(row.getCell(4));
                Long registroCalificado = null;
                if (registroStr != null && !registroStr.isEmpty() && registroStr.matches("\\d+")) {
                    registroCalificado = Long.parseLong(registroStr);
                } else {
                    System.out.println("Valor inválido para registro calificado en fila " + (row.getRowNum() + 1) + ": " + registroStr);
                }

                Date fechaRegistroCalificado = parseExcelDate(row.getCell(5));

                String acuardosCreacion = getCellValueAsString(row.getCell(6));
                String tipoAcuardosCreacion = getCellValueAsString(row.getCell(7));

                String acreditacionAltaCalidad = getCellValueAsString(row.getCell(8));

                Date fechaAcreditacion = parseExcelDate(row.getCell(9));

                AcademicUnit unidad = academicUnitRepository
                        .findAll()
                        .stream()
                        .filter(u -> u.getNombre().equalsIgnoreCase(unidadAcademicaNombre))
                        .findFirst()
                        .orElseGet(() -> academicUnitRepository.save(
                                AcademicUnit.builder().nombre(unidadAcademicaNombre).build()
                        ));

                AgreementType tipo = agreementTypeRepository
                        .findAll()
                        .stream()
                        .filter(t -> t.getNombre().equalsIgnoreCase(tipoAcuardosCreacion))
                        .findFirst()
                        .orElseGet(() -> agreementTypeRepository.save(
                                AgreementType.builder().nombre(tipoAcuardosCreacion).build()
                        ));

                program.setCodigo(codigo);
                program.setNombre(nombre);
                program.setUnidadAcademica(unidad);
                program.setSnies(snies);
                program.setAcuardosCreacion(acuardosCreacion);
                program.setTipoAcuerdo(tipo);
                program.setAcreditacionAltaCalidad(acreditacionAltaCalidad);
                program.setRegistroCalificado(registroCalificado);
                program.setFechaRegistroCalificado(fechaRegistroCalificado);
                program.setFechaAcreditacion(fechaAcreditacion);

                programsToSave.add(program);
            }

            programRepository.saveAll(programsToSave);
        }
    }

    private String getCellValueAsString(Cell cell){
        if(cell == null) return null;

        switch (cell.getCellType()){
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)){
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    long longVal = (long) val;
                    if(val == longVal){
                        return String.valueOf(longVal);
                    } else {
                        return String.valueOf(val);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try{
                    return cell.getStringCellValue();
                } catch (IllegalStateException e){
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return null;
        }
    }

    private Date parseExcelDate(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return new Date(cell.getDateCellValue().getTime());
            } else {
                return new Date(DateUtil.getJavaDate(cell.getNumericCellValue()).getTime());
            }
        } else if (cell.getCellType() == CellType.STRING) {
            String text = cell.getStringCellValue().trim();
            if (text.isEmpty()) return null;
            try {
                return Date.valueOf(LocalDate.parse(text));
            } catch (Exception e) {
                System.out.println("No se pudo parsear la fecha como texto: " + text);
                return null;
            }
        }

        return null;
    }

}
