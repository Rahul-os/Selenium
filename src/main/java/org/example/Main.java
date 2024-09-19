package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Selenium\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("D:\\MS\\sem-4\\Software Testing\\Calculator.html");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test Results");

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Operand 1");
        row.createCell(1).setCellValue("Operand 2");
        row.createCell(2).setCellValue("Operator");
        row.createCell(3).setCellValue("Result/ Actual Output");
        row.createCell(4).setCellValue("Expected Result");


        for (int i = 0; i < 1000 ; i++) {
            Random random = new Random();
            int firstValue = random.nextInt(9);
            int secondValue = random.nextInt(9);
            System.out.println("First value:" + firstValue);
            System.out.println("Second value:" + secondValue);
            int operand = random.nextInt(4);
            System.out.println("operand" + operand);
            String[] operation = {"+","-","*","/"};
            int expectedResult;


            driver.findElement(By.xpath("//*[@id=\"num" + firstValue + "\"]")).click();
            Thread.sleep(10);
            if (operand == 0) {
                driver.findElement(By.xpath("//*[@id=\"add\"]")).click();
                expectedResult = firstValue + secondValue;
            } else if (operand == 1) {
                driver.findElement(By.xpath("//*[@id=\"sub\"]")).click();
                expectedResult = firstValue - secondValue;
            } else if (operand == 2) {
                driver.findElement(By.xpath("//*[@id=\"mul\"]")).click();
                expectedResult = firstValue * secondValue;
            } else {
                try{
                    driver.findElement(By.xpath("//*[@id=\"div\"]")).click();
                    expectedResult = firstValue / secondValue;
                }
                catch (ArithmeticException ex) {            // handling Divide ByZero exception
                    continue;    // when ArithmeticException(DivideByZero) occurs, the values will be skipped, and the fields in excel file will be left empty.
                }
            }
            //Thread.sleep(100);
            driver.findElement(By.xpath("//*[@id=\"num" + secondValue + "\"]")).click();
            //Thread.sleep(100);
            //driver.findElement(By.xpath("/html/body/div[1]/div/button[17]")).click();
            WebElement equalsButton = driver.findElement(By.xpath("//button[@value='=']"));     // user defined Xpath
            equalsButton.click();
            WebElement resultElement = driver.findElement(By.id("screen"));
            String actualResult = resultElement.getAttribute("value");
            System.out.println("Expected res:" + expectedResult + " Actual res: " + actualResult);   // Compare the expected output with the actual output.
            driver.findElement(By.xpath("/html/body/div/div/button[16]")).click();

            Row dataRow = sheet.createRow(i + 1);
            dataRow.createCell(0).setCellValue(firstValue);
            dataRow.createCell(1).setCellValue(secondValue);
            dataRow.createCell(2).setCellValue((operation[operand]));
            dataRow.createCell(3).setCellValue(actualResult);
            dataRow.createCell(4).setCellValue(expectedResult);


            try{
                FileOutputStream fileOut = new FileOutputStream("test-results.xlsx");
                workbook.write(fileOut);
            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        }

    }
}