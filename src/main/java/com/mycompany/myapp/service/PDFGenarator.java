package com.mycompany.myapp.service;

import static java.lang.Integer.parseInt;
import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.repository.SelectedOptionRepository;
import com.mycompany.myapp.repository.SelectionsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import net.sf.jasperreports.engine.*;
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

    private final BooksRepository booksRepository;

    public PDFGenarator(
        SelectedOptionRepository selectedOptionRepository,
        SelectionsRepository selectionsRepository,
        BooksRepository booksRepository
    ) {
        this.selectedOptionRepository = selectedOptionRepository;
        this.selectionsRepository = selectionsRepository;
        this.booksRepository = booksRepository;
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
        JasperPrint jasperPrint = new JasperPrint();
        for (BooksPage booksPage : books.getBooksPages()) {
            //sort PageLayers in ascending Order
            BooksPage booksPages = sortPageLayer(booksPage);
            //create page & add to jasper list

            jasperPrint = createPageInner(booksPages, jasperDesign, books);
        }
        jasperPrintList.add(jasperPrint);
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
                    band.addElement(layers.getLayerNo(), createEditableImages(selections, jasperDesign));
                } else {
                    band.addElement(layers.getLayerNo(), createNonEditableImages(configMap, jasperDesign));
                }
            } else {
                if (layers.getIsEditable()) {
                    String text = configMap.get("text");
                    SelectedOption selectedOption = selectedOptionRepository.findOneByCodeAndBooks_Code("editableText", "DEMO");
                    Set<SelectedOptionDetails> editableText = selectedOption.getSelectedOptionDetails();
                    band.addElement(layers.getLayerNo(), createEditableText(text, editableText, configMap));
                } else {
                    String text = configMap.get("text");
                    band.addElement(layers.getLayerNo(), createNonEditableText(text, configMap));
                }
            }
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

    JRDesignImage createEditableImages(Selections selections, JasperDesign jasperDesign) {
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

    JRDesignImage createNonEditableImages(Map<String, String> configMap, JasperDesign jasperDesign) {
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText("\"" + configMap.get("image") + "\"");
        JRDesignImage image = new JRDesignImage(jasperDesign);
        image.setX(parseInt(configMap.get("x")));
        image.setY(parseInt(configMap.get("y")));
        image.setWidth(parseInt(configMap.get("width")));
        image.setHeight(parseInt(configMap.get("height")));
        image.setExpression(expression);
        return image;
    }

    JRDesignStaticText createEditableText(String text, Set<SelectedOptionDetails> editableTex, Map<String, String> configMap) {
        for (SelectedOptionDetails selectedOptionDetails : editableTex) {
            String target = selectedOptionDetails.getCode();
            String value = selectedOptionDetails.getSelectedValue();
            text = text.replace(target, value);
        }
        JRDesignStaticText staticText = new JRDesignStaticText();

        staticText.setX(parseInt(configMap.get("x")));
        staticText.setY(parseInt(configMap.get("y")));
        staticText.setWidth(parseInt(configMap.get("width")));
        staticText.setHeight(parseInt(configMap.get("height")));
        staticText.setFontSize(Float.parseFloat(configMap.get("fontSize")));
        staticText.setForecolor(Color.BLACK);
        staticText.setPdfFontName(configMap.get("fontName"));
        staticText.setPdfEncoding("Cp1252");
        staticText.setPdfEmbedded(true);
        staticText.setMode(ModeEnum.TRANSPARENT);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        staticText.setText(text);
        return staticText;
    }

    JRDesignStaticText createNonEditableText(String text, Map<String, String> configMap) {
        JRDesignStaticText staticText = new JRDesignStaticText();

        staticText.setX(parseInt(configMap.get("x")));
        staticText.setY(parseInt(configMap.get("y")));
        staticText.setWidth(parseInt(configMap.get("width")));
        staticText.setHeight(parseInt(configMap.get("height")));
        staticText.setFontSize(Float.parseFloat(configMap.get("fontSize")));
        staticText.setForecolor(Color.BLACK);
        staticText.setPdfFontName(configMap.get("fontName"));
        staticText.setPdfEncoding("Cp1252");
        staticText.setPdfEmbedded(true);
        staticText.setMode(ModeEnum.TRANSPARENT);
        staticText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        staticText.setText(text);
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

    Map<String, String> getTemplateText(String customerCode) {
        Map<String, String> templateText = new HashMap<>();
        SelectedOption selectedOption = selectedOptionRepository.findOneByCodeAndBooks_Code(customerCode, "DEMO");
        Optional<Books> books = booksRepository.findOneByCode("DEMO");
        if (books.isPresent()) {
            Books book = books.get();
            for (SelectedOptionDetails selectedOptionDetails : selectedOption.getSelectedOptionDetails()) {}
        } else {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }

        return templateText;
    }
}
