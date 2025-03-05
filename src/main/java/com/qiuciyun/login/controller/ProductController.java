package com.qiuciyun.login.controller;

import com.qiuciyun.login.entity.Product;
import com.qiuciyun.login.service.ProductService;
import com.qiuciyun.login.common.Result;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result<List<Product>> getAllProducts() {
        return Result.success(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @PostMapping
    public Result<Product> createProduct(@RequestBody Product product) {
        return Result.success(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success("产品删除成功");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            
            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("产品列表");

            // 创建标题行样式
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] columnHeaders = {"ID", "产品名称", "描述", "价格", "库存", "类别", "创建时间", "更新时间", "状态"};
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // 设置列宽
            }

            // 创建数据行
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(workbook.createDataFormat().getFormat("@"));
            
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                
                // ID列
                Cell idCell = row.createCell(0);
                if (product.getId() != null) {
                    idCell.setCellValue(product.getId());
                }

                // 名称列
                Cell nameCell = row.createCell(1);
                nameCell.setCellStyle(textStyle);
                if (product.getName() != null) {
                    nameCell.setCellValue(product.getName());
                }

                // 描述列
                Cell descCell = row.createCell(2);
                descCell.setCellStyle(textStyle);
                if (product.getDescription() != null) {
                    descCell.setCellValue(product.getDescription());
                }

                // 价格列
                Cell priceCell = row.createCell(3);
                if (product.getPrice() != null) {
                    priceCell.setCellValue(product.getPrice().doubleValue());
                }

                // 库存列
                Cell stockCell = row.createCell(4);
                if (product.getStock() != null) {
                    stockCell.setCellValue(product.getStock());
                }

                // 类别列
                Cell categoryCell = row.createCell(5);
                categoryCell.setCellStyle(textStyle);
                if (product.getCategory() != null) {
                    categoryCell.setCellValue(product.getCategory());
                }

                // 创建时间列
                Cell createTimeCell = row.createCell(6);
                createTimeCell.setCellStyle(textStyle);
                if (product.getCreateTime() != null) {
                    createTimeCell.setCellValue(product.getCreateTime().format(formatter));
                }

                // 更新时间列
                Cell updateTimeCell = row.createCell(7);
                updateTimeCell.setCellStyle(textStyle);
                if (product.getUpdateTime() != null) {
                    updateTimeCell.setCellValue(product.getUpdateTime().format(formatter));
                }

                // 状态列
                Cell statusCell = row.createCell(8);
                statusCell.setCellStyle(textStyle);
                if (product.getStatus() != null) {
                    statusCell.setCellValue(product.getStatus() == 1 ? "上架" : "下架");
                }
            }

            // 导出文件
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            // 设置响应头
            String filename = String.format("产品列表_%s.xlsx", 
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDispositionFormData("attachment", 
                new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

            return ResponseEntity
                    .ok()
                    .headers(responseHeaders)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
} 