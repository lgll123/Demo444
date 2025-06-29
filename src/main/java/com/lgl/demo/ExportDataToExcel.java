package com.lgl.demo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class ExportDataToExcel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testmysql";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD)) {
            // 获取系统临时目录
            String tempDir = System.getProperty("java.io.tmpdir");
            System.out.println(tempDir);
            Path filePath = Paths.get(tempDir,  "知识目录数据" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
            // 查询数据
            List<Map<String, Object>> data = queryData(conn);
            // 构建树形结构 
            Map<Integer, List<Map<String, Object>>> tree = buildTree(data);
            // 创建 Excel 文件 
            Workbook workbook = createExcelWorkbook(tree);
            // 保存 Excel 文件 
            saveExcel(workbook, filePath.toFile());
            System.out.println(" 数据已成功导出到 knowledge_catalog.xlsx");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> queryData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM tbl_knowledge_catalog WHERE delete_flag = '0'";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())  {
            List<Map<String, Object>> data = new ArrayList<>();
            while (rs.next())  {
                Map<String, Object> row = new HashMap<>();
                row.put("id",  rs.getInt("id"));
                row.put("pid",  rs.getInt("pid"));
                row.put("level",  rs.getString("level"));
                row.put("catalog_name",  rs.getString("catalog_name"));
                row.put("dept_id",  rs.getString("dept_id"));
                row.put("team_id",  rs.getString("team_id"));
                data.add(row);
            }
            return data;
        }
    }

    private static Map<Integer, List<Map<String, Object>>> buildTree(List<Map<String, Object>> data) {
        Map<Integer, List<Map<String, Object>>> tree = new HashMap<>();
        for (Map<String, Object> row : data) {
            int pid = (int) row.getOrDefault("pid",  0);
            tree.computeIfAbsent(pid,  k -> new ArrayList<>()).add(row);
        }
        return tree;
    }

    private static Workbook createExcelWorkbook(Map<Integer, List<Map<String, Object>>> tree) {
        //获取知识目录层级
        String level = "4"; //todo
        //构建表头
        String[] levelArr = {"一级", "二级", "三级", "四级","五级"};
        String[] result = new String[Integer.valueOf(level)];
        System.arraycopy(levelArr,  0, result, 0, Integer.valueOf(level));

        String[] deptArr = {"维护部门", "维护团队"};

        String[] headers = Stream.concat(
                Arrays.stream(result),
                Arrays.stream(deptArr)
        ).toArray(String[]::new);

        // 设置sheet表头
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("知识目录");
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length;  i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 写入数据 
        int rowIndex = 1;
        List<Map<String, Object>> rootNodes = tree.getOrDefault(0,  new ArrayList<>());
        for (Map<String, Object> rootNode : rootNodes) {
            rowIndex = writeNode(sheet, tree, rootNode, rowIndex, 0);
        }
        // 自动调整列宽
        for (int i = 0; i < headers.length;  i++) {
//            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, 20 * 256);
        }
        //删除空行
        removeEmptyRows(sheet);

        return workbook;
    }

    private static int writeNode(Sheet sheet, Map<Integer, List<Map<String, Object>>> tree, Map<String, Object> node, int rowIndex, int level) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.createCell(level);
        cell.setCellValue((String)  node.get("catalog_name"));

        Cell deptCell = row.createCell(4);
        deptCell.setCellValue((String)  node.get("dept_id"));

        Cell teamCell = row.createCell(5);
        teamCell.setCellValue((String)  node.get("team_id"));

        int id = (int) node.get("id");

        List<Map<String, Object>> children = tree.getOrDefault(id,  new ArrayList<>());
        for (Map<String, Object> child : children) {
            rowIndex = writeNode(sheet, tree, child, rowIndex, level + 1);
        }

        return rowIndex + 1;
    }

    private static void saveExcel(Workbook workbook, File file) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
    }

    public static void removeEmptyRows(Sheet sheet) {
        for (int i = sheet.getLastRowNum();  i >= 0; i--) {  // 从最后一行开始遍历
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {  // 判断行是否为空
                //使用 shiftRows 上移后续行：
                 sheet.shiftRows(i  + 1, sheet.getLastRowNum(),  -1);
            }
        }
    }
    // 辅助方法：检查行是否为空
    private static boolean isRowEmpty(Row row) {
        for (int j = 0; j < row.getLastCellNum();  j++) {
            Cell cell = row.getCell(j);
            if (cell != null && cell.getCellType()  != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
} 