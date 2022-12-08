package com.mycompany.myapp.service;

import static java.lang.Integer.parseInt;

import com.mycompany.myapp.domain.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
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

    public byte[] pdfCreator(Books books) throws IOException, JRException {
        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

        JasperDesign jasperDesign = createPage(books);
        for (BooksPage booksPage : books.getBooksPages()) {
            jasperPrintList.add(createPageInner(sortPageLayer(booksPage), jasperDesign)); //sort ,create page & add to jasper list
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

    JasperPrint createPageInner(BooksPage booksPage, JasperDesign jasperDesign) {
        JRDesignBand band = new JRDesignBand();
        band.setHeight(1122);
        //        band.setSplitType(SplitTypeEnum.STRETCH);

        SimpleJasperReportsContext jasperReportsContext = new SimpleJasperReportsContext();

        for (PageLayers layers : booksPage.getPageDetails()) {
            Map<String, String> configMap = new HashMap<>();
            for (PageLayersDetails layerDetails : layers.getPageElementDetails()) {
                configMap.put(layerDetails.getName(), layerDetails.getDescription());
            }
            if (configMap.get("type").equals("text")) {} else if (configMap.get("type").equals("img")) {
                band.addElement(layers.getLayerNo(), createImage(configMap, jasperDesign));
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

    JRDesignStaticText createText(String text, int x, int y) {
        JRDesignStaticText staticText = new JRDesignStaticText();

        staticText.setX(x);
        staticText.setY(y);
        staticText.setWidth(300);
        staticText.setHeight(100);
        staticText.setFontSize(70F);
        staticText.setForecolor(Color.BLUE);
        staticText.setFontName("Dancing Script");
        staticText.setPdfFontName("https://alphadevs-logos.s3.ap-south-1.amazonaws.com/Teko-Bold.ttf");
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
}
