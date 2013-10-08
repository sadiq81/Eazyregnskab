package dk.eazyit.eazyregnskab.services.importer;

import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import dk.eazyit.eazyregnskab.services.importer.financeaccount.FinanceAccountLine;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Component
public class ImportParser {

    final static Logger logger = LoggerFactory.getLogger(ImportParser.class);

    public List<FinanceAccountLine> parseImportedFinanceAccountsXls(File file) {

        List<FinanceAccountLine> list = new ArrayList<>();
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");
            Workbook w = Workbook.getWorkbook(file, ws);
            Sheet sheet = w.getSheet(0);
            for (int row = 0; row < (sheet.getRows() > 500 ? 500 : sheet.getRows()); row++) {
                FinanceAccountLine line = new FinanceAccountLine();
                line.setAccountNumber(sheet.getCell(0, row).getContents());
                line.setAccountName(sheet.getCell(1, row).getContents());
                line.setVatType(sheet.getCell(2, row).getContents());
                line.setAccountFrom(sheet.getCell(3, row).getContents());
                line.setAccountTo(sheet.getCell(4, row).getContents());
                try {
                    line.setType(FinanceAccountType.valueOf(sheet.getCell(5, row).getContents()));
                } catch (IllegalArgumentException e) {
                    logger.debug("Could not understand enum value of " + sheet.getCell(5, row).getContents());
                }
                list.add(line);
            }
        } catch (IOException exception) {

        } catch (BiffException exception) {

        }
        return list;
    }

}
