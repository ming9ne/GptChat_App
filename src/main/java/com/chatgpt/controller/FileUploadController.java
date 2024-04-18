package com.chatgpt.controller;

import com.chatgpt.model.StudentInfo;
import com.chatgpt.repository.StudentRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try {
            // 엑셀 파일을 읽어옴
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // 헤더 정보를 읽음
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // 데이터를 읽어와서 처리
            List<StudentInfo> users = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                StudentInfo user = new StudentInfo();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i);
                    String value = "";
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    value = cell.getDateCellValue().toString();
                                } else {
                                    // 숫자 값을 문자열로 변환할 때 빈 문자열인지 확인하여 처리
                                    value = cell.getNumericCellValue() != 0 ? String.valueOf((long) cell.getNumericCellValue()) : "";
                                }
                                break;
                            default:
                                value = cell.toString();
                        }
                    }
                    switch (headers.get(i)) {
                        case "id":
                            user.setUserId(value);
                            break;
                        case "username":
                            user.setUsername(value);
                            break;
                        case "phone":
                            user.setPhone(value);
                            break;
                        case "role":
                            user.setRole(value);
                            break;
                        default:
                            // 추가 필드가 있을 경우 처리
                    }
                }
                users.add(user);
            }

            // 데이터베이스에 저장
            studentRepository.saveAll(users);

            // 콘솔에 업로드된 파일 내용 출력
            System.out.println("Uploaded file content:");
            users.forEach(u -> System.out.println(u.toString()));

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException | IllegalStateException | InvalidFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
