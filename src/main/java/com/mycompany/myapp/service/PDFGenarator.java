package com.mycompany.myapp.service;

import static java.lang.Integer.parseInt;
import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import com.mycompany.myapp.repository.SelectionsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.type.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PDFGenarator {

    private final SelectedOptionRepository selectedOptionRepository;
    private final SelectionsRepository selectionsRepository;

    public PDFGenarator(SelectedOptionRepository selectedOptionRepository, SelectionsRepository selectionsRepository) {
        this.selectedOptionRepository = selectedOptionRepository;
        this.selectionsRepository = selectionsRepository;
    }

    public byte[] pdfCreator(Books books) throws IOException, JRException {
        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

        JasperDesign jasperDesign = createPage(books);

        // is page size null
        if (books.getBooksPages().size() == 0) {
            throw new BadRequestAlertException("A new books cannot have pages", ENTITY_NAME, "empty pages");
        }

        //sort BooksPage in ascending Order
        books = sortBooksPage(books);

        for (BooksPage booksPage : books.getBooksPages()) {
            //sort PageLayers in ascending Order
            BooksPage booksPages = sortPageLayer(booksPage);
            //create page & add to jasper list
            JasperPrint jasperPrint = new JasperPrint();

            JRPrintPage a = new JRBasePrintPage();

            jasperPrint.addPage(a);
            jasperPrint = createPageInner(booksPages, jasperDesign, books);
            jasperPrintList.add(jasperPrint);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        //Add the list as a Parameter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
        //this will make a bookmark in the exported PDF for each of the reports
        exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
        exporter.exportReport();
        return baos.toByteArray();
    }

    JasperDesign createPage(Books books) {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("The dynamically generated report");
        jasperDesign.setPageWidth(books.getPageSize().getWidth());
        jasperDesign.setPageHeight(books.getPageSize().getHeight());
        jasperDesign.setColumnWidth(books.getPageSize().getWidth());
        jasperDesign.setLeftMargin(0);
        jasperDesign.setRightMargin(0);
        jasperDesign.setTopMargin(0);
        jasperDesign.setBottomMargin(0);
        jasperDesign.setTitleNewPage(false);
        jasperDesign.setFloatColumnFooter(false);
        jasperDesign.setSummaryNewPage(false);
        return jasperDesign;
    }

    JasperPrint createPageInner(BooksPage booksPage, JasperDesign jasperDesign, Books books) {
        JRDesignBand band = new JRDesignBand();
        band.setHeight(books.getPageSize().getHeight());
        //        band.setSplitType(SplitTypeEnum.STRETCH);

        SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();

        for (PageLayers layers : booksPage.getPageDetails()) {
            Map<String, String> configMap = new HashMap<>();
            for (PageLayersDetails layerDetails : layers.getPageElementDetails()) {
                configMap.put(layerDetails.getName(), layerDetails.getDescription());
            }

            if (!layers.getIsText()) {
                if (layers.getIsEditable()) {
                    String characterCode = configMap.get("characterCode");
                    SelectedOption selectedOption = selectedOptionRepository.findOneByCodeAndBooks_Code("A", "DEMO");
                    Map<String, String> selectedMap = new HashMap<>();
                    for (SelectedOptionDetails selectedOptionDetails : selectedOption.getSelectedOptionDetails()) {
                        selectedMap.put(selectedOptionDetails.getName(), selectedOptionDetails.getSelectedValue());
                    }
                    Selections selections = selectionsRepository.findOneByAvatarCodeAndStyleCodeAndOptionCode(
                        "A",
                        selectedMap.get("styleCode"),
                        selectedMap.get("optionCode")
                    );
                    band.addElement(layers.getLayerNo(), createImages(selections, jasperDesign));
                } else {
                    band.addElement(layers.getLayerNo(), createImage(configMap, jasperDesign));
                }
            } else {
                if (layers.getIsEditable()) {
                    //                    String editableTextCode = configMap.get("editableTextCode");
                    String text = configMap.get("text");
                    //                    SelectedOption selectedOption =selectedOptionRepository.findOneByCodeAndBooks_Code(editableTextCode, "DEMO");
                    //                    SelectedOption selectedOption = selectedOptionRepository.findOneByCodeAndBooks_Code("A", "DEMO");
                    //                    Map<String, String> selectedMap = new HashMap<>();
                    //                    for (SelectedOptionDetails selectedOptionDetails : selectedOption.getSelectedOptionDetails()) {
                    //                        selectedMap.put(selectedOptionDetails.getName(), selectedOptionDetails.getSelectedValue());
                    //                    }
                    //                    Selections selections = selectionsRepository.findOneByAvatarCodeAndStyleCodeAndOptionCode(
                    //                        "A",
                    //                        selectedMap.get("styleCode"),
                    //                        selectedMap.get("optionCode")
                    //                    );
                    band.addElement(layers.getLayerNo(), createText("dilshan", "dilshan", 0, 0));
                }
            }
            //            if (configMap.get("type").equals("text")) {}
            //            else if (configMap.get("type").equals("img")) {
            //                band.addElement(layers.getLayerNo(), createImage(configMap, jasperDesign));
            //            }
        }
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(band);

        Collection<BooksPage> details = new HashSet<>();
        details.add(booksPage);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(details);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "AlphaDevs");
        JasperReport jasperReport = null;
        JasperPrint jasperPrint = null;
        try {
            jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.getInstance(jasperReportsContext).fill(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return jasperPrint;
    }

    JRDesignImage createImages(Selections selections, JasperDesign jasperDesign) {
        JRDesignExpression expression = new JRDesignExpression();
        //       expression.setText("\"https://wikunum-lite-generic.s3.ap-south-1.amazonaws.com/storeImages/ADPanicBuying1665638023.jpeg\"");
        expression.setText("\"" + selections.getImage() + "\"");
        JRDesignImage image = new JRDesignImage(jasperDesign);
        image.setX(selections.getX());
        image.setY(selections.getY());
        image.setWidth(selections.getWidth());
        image.setHeight(selections.getHeight());
        image.setExpression(expression);
        return image;
    }

    JRDesignImage createImage(Map<String, String> configMap, JasperDesign jasperDesign) {
        JRDesignExpression expression = new JRDesignExpression();
        //       expression.setText("\"https://wikunum-lite-generic.s3.ap-south-1.amazonaws.com/storeImages/ADPanicBuying1665638023.jpeg\"");
        expression.setText("\"" + configMap.get("image") + "\"");
        JRDesignImage image = new JRDesignImage(jasperDesign);
        image.setX(parseInt(configMap.get("x")));
        image.setY(parseInt(configMap.get("y")));
        image.setWidth(parseInt(configMap.get("width")));
        image.setHeight(parseInt(configMap.get("height")));
        image.setExpression(expression);
        return image;
    }

    JRDesignStaticText createText(String text, String editableTextCode, int x, int y) {
        String before = "{{userName}}'s Best Friends";
        System.out.println(before);
        String after = before.replace("{{userName}}", text);

        JRDesignStaticText staticText = new JRDesignStaticText();

        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(500);
        staticText.setHeight(200);
        staticText.setFontSize(50F);
        staticText.setForecolor(Color.BLACK);
        staticText.setFontName("Dancing Script");
        staticText.setPdfFontName("https://alphadevs-logos.s3.ap-south-1.amazonaws.com/DancingScript-Bold.ttf");
        staticText.setPdfEncoding("Cp1252");
        staticText.setPdfEmbedded(true);
        staticText.setMode(ModeEnum.TRANSPARENT);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        staticText.setText(after);
        return staticText;
    }

    //Sort the layers in ascending order
    BooksPage sortPageLayer(BooksPage booksPage) {
        List<PageLayers> layers = new ArrayList<>(booksPage.getPageDetails());
        Collections.sort(
            layers,
            new Comparator<PageLayers>() {
                public int compare(PageLayers o1, PageLayers o2) {
                    return o1.getLayerNo().compareTo(o2.getLayerNo());
                }
            }
        );
        Set<PageLayers> targetSet = new HashSet<>(layers);
        booksPage.setPageDetails(targetSet);

        return booksPage;
    }

    Books sortBooksPage(Books books) {
        List<BooksPage> pages = new ArrayList<>(books.getBooksPages());
        Collections.sort(
            pages,
            new Comparator<BooksPage>() {
                public int compare(BooksPage o1, BooksPage o2) {
                    return o1.getNum().compareTo(o2.getNum());
                }
            }
        );
        Set<BooksPage> targetSet = new HashSet<>(pages);
        books.setBooksPages(targetSet);

        return books;
    }

    void editableTextArray(String text) {}

    SelectedOption getCustomerSelectedOption() {
        return null;
    }
}
