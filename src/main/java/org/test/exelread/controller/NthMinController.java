package org.test.exelread.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.exelread.service.NthMinService;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NthMinController {

    private final NthMinService service;

    @Operation(summary = "Вернуть N‑ое минимальное число из первого столбца указанного .xlsx файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Число успешно найдено"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса или ошибка чтения файла"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/nth-min")
    public ResponseEntity<?> nthMin(
            @Parameter(description = "Абсолютный или относительный путь к файлу .xlsx", example = "/path/to/file.xlsx")
            @RequestParam("filePath") String filePath,
            @Parameter(description = "Номер искомого минимального элемента (начинается с 1)", example = "3")
            @RequestParam("n") int n) {
        try {
            int value = service.findNthMin(filePath, n);
            return ResponseEntity.ok(value);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка в параметрах запроса: " + e.getMessage());
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Файл не найден: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }
}
