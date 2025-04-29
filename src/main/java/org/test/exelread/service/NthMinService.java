package org.test.exelread.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NthMinService {

    /**
     * Считывает целые числа из первого столбца первого листа Excel-файла
     * и возвращает N‑ое минимальное значение.
     *
     * @param path путь к .xlsx файлу
     * @param n номер минимального элемента, который нужно найти (начинается с 1)
     * @return N‑ое минимальное число
     * @throws IOException если файл не найден или не читается
     */
    public int findNthMin(String path, int n) throws IOException {
        List<Integer> numbers = load(path);

        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("Файл не содержит чисел в первом столбце.");
        }

        if (n < 1) {
            throw new IllegalArgumentException("n должно быть больше или равно 1.");
        }
        if (n > numbers.size()) {
            throw new IllegalArgumentException("n превышает количество доступных чисел (" + numbers.size() + ").");
        }

        return quickSelect(numbers, 0, numbers.size() - 1, n - 1);
    }

    /**
     * Загружает все целые числа из первого столбца первого листа файла.
     */
    private List<Integer> load(String path) throws IOException {
        try (FileInputStream in = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(in)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Integer> result = new ArrayList<>();

            for (Row row : sheet) {
                Cell c = row.getCell(0);
                if (c != null && c.getCellType() == CellType.NUMERIC) {
                    result.add((int) c.getNumericCellValue());
                }
            }

            return result;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл не найден: " + path);
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении файла: " + e.getMessage(), e);
        }
    }

    // ===== Реализация алгоритма быстрой выборки (Quickselect) =====

    private int quickSelect(List<Integer> arr, int left, int right, int k) {
        if (left == right) return arr.get(left);

        int pivotIndex = partition(arr, left, right);

        if (k == pivotIndex) {
            return arr.get(k);
        } else if (k < pivotIndex) {
            return quickSelect(arr, left, pivotIndex - 1, k);
        } else {
            return quickSelect(arr, pivotIndex + 1, right, k);
        }
    }

    private int partition(List<Integer> arr, int left, int right) {
        int pivot = arr.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr.get(j) < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, right);
        return i;
    }

    private void swap(List<Integer> arr, int i, int j) {
        int tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }
}
