import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class CheckFilesTest {

    @Test
    void checkTxtFileTest() throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("txt.txt")) {
            var textFromFile = new String(stream.readAllBytes(), "UTF-8");
            assertThat(textFromFile).contains("Hello world!");
        }
    }

    @Test
    void checkPdfFileTest() throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("pdf.pdf")) {
            PDF pdfFile = new PDF(stream);
            assertThat(pdfFile.text).contains("Hello world!");
        }
    }

    @Test
    void checkExcelFileTest() throws IOException {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("xlsx.xlsx")) {
            XLS xlsFile = new XLS(stream);
            var row = xlsFile.excel.getSheetAt(0).getRow(0);

            assertThat(row.getCell(0).getStringCellValue()).isEqualTo("Hello");
            assertThat(row.getCell(1).getStringCellValue()).isEqualTo("World");
        }
    }

    @Test
    void checkRarWithPasswordTest() throws IOException, ZipException {
        String passwordFromZipFile = "1234";
        String pathToExtractFilesFromZip = "build/zipFiles";

        var zipFileURL = getClass().getClassLoader().getResource("zip.zip");
        var zipFile = new ZipFile(new File(zipFileURL.getPath()));

        if (zipFile.isEncrypted())
            zipFile.setPassword(passwordFromZipFile);
       zipFile.extractAll(pathToExtractFilesFromZip);

       var pdfFilePath = new File(String.format("%s/FileInZIp.pdf", pathToExtractFilesFromZip));
       var pdfFile = new PDF(pdfFilePath);
       assertThat(pdfFile.text).contains("PDF Test File");
    }

}
