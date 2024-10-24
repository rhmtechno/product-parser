package com.java.product.parser.service.impl;
import com.java.product.parser.domain.entity.Product;
import com.java.product.parser.service.AbstractParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;


@Service
@Slf4j
@RequiredArgsConstructor
public class XlsxParserImplService implements AbstractParser {

    private final ProductService productService; ;

    @Override
    public void parse(InputStream inputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip the header
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Ensure the row has enough cells
                if (row.getPhysicalNumberOfCells() < 4) {
                    log.warn("Row {} does not have enough data, skipping.", row.getRowNum());
                    continue; // Skip rows that do not have enough data
                }

                // Extract data from the row
                String sku = row.getCell(0).getStringCellValue();
                String title = row.getCell(1).getStringCellValue();
                BigDecimal price = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
                Integer quantity = (int) row.getCell(3).getNumericCellValue();

                // Find existing product
                Product product = productService.getProductBySku(sku);

                if (product == null) {
                    log.info("Adding new product with SKU: {}", sku);
                    productService.saveOrUpdate(new Product(sku, title, price, quantity));
                } else {
                    String changeStatus = checkProductChanges(product, title, price, quantity);
                    switch (changeStatus) {
                        case "UPDATED":
                            log.info("Updating product with SKU: {}", sku);
                            product.setTitle(title);
                            product.setPrice(price);
                            product.setQuantity(quantity);
                            productService.saveOrUpdate(product);
                            break;
                        case "UNCHANGED":
                            log.info("Product with SKU: {} is unchanged", sku);
                            break;
                        default:
                            log.warn("Unexpected case for SKU: {}", sku);
                    }
                }
            }
        }
    }




}