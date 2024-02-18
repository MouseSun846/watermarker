package com.lsy.springcloud;
//import com.itextpdf.io.font.FontProgramFactory;
//import com.itextpdf.io.font.constants.StandardFonts;
//import com.itextpdf.io.image.ImageData;
//import com.itextpdf.io.image.ImageDataFactory;
//import com.itextpdf.kernel.colors.ColorConstants;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.geom.PageSize;
//import com.itextpdf.kernel.pdf.*;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
//import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
//import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
//import com.itextpdf.layout.Canvas;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Image;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.properties.Property;
//import com.itextpdf.layout.properties.TextAlignment;
//import com.itextpdf.layout.properties.VerticalAlignment;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class App {
    public static final String SRC = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见.pdf";
    public static final String DEST = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见_水印.pdf";
    public static final String DEST_PIC = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见_水印_PIC.pdf";
    public static final String WATERMARK = "LSY-刘尚阳";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        long start = System.currentTimeMillis();
//        App app = new App();
//        app.manipulatePdf(DEST);
//        app.convertPdfPageToImage();
//        app.getPdfInfo();
        long stop = System.currentTimeMillis();
        System.out.println((stop - start) + "ms");
    }

//    protected void manipulatePdf(String dest) throws Exception {
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
//        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
//        Paragraph paragraph = new Paragraph("This watermark 飞鸟云 is added UNDER the existing content")
//                .setFont(font)
//                .setFontSize(15);
//        PdfCanvas over = new PdfCanvas(pdfDoc.getFirstPage());
//        // 设置颜色
//        over.setFillColor(ColorConstants.LIGHT_GRAY);
//        over.saveState();
//        PdfExtGState gs1 = new PdfExtGState();
//        // 设置水印透明状态
//        gs1.setFillOpacity(0.5f);
//        over.setExtGState(gs1);
//        Canvas canvasWatermark3 = new Canvas(over, pdfDoc.getDefaultPageSize())
//                .showTextAligned(paragraph, 297, 450, 1, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
//        canvasWatermark3.close();
//        over.restoreState();
//        pdfDoc.close();
//    }

//    protected void manipulatePdf(String dest) throws Exception {
//        WriterProperties wp = new WriterProperties();
//        //禁止打印 复制 等操作
////        wp.setStandardEncryption(null, null, 0, EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
//
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
//        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
//        Paragraph paragraph = new Paragraph("This watermark 飞鸟云")
//                .setFont(font)
//                .setFontSize(15);
//
//        for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
//            PdfPage page = pdfDoc.getPage(i + 1); // iText页面索引从1开始
//
//            // 计算每页的高度，以便平均分布水印
//            float pageHeight = page.getPageSize().getHeight();
//            float lineHeight = pageHeight / 4; // 将页面高度分成4行，其中3行用于水印
//
//            PdfCanvas over = new PdfCanvas(page);
//            over.setFillColor(ColorConstants.GRAY);
//            over.saveState();
//            PdfExtGState gs1 = new PdfExtGState();
//            gs1.setFillOpacity(0.5f);
//            over.setExtGState(gs1);
//
//            // 在页面上创建三行水印
//            for (int j = 0; j < 3; j++) {
//                float yPosition = lineHeight * (j + 1); // 计算每行水印的y坐标
//                Canvas canvasWatermark = new Canvas(over, page.getPageSize())
//                        .showTextAligned(paragraph, page.getPageSize().getWidth() / 2, yPosition, 1,
//                                TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
//                canvasWatermark.close();
//            }
//
//            over.restoreState();
//        }
//
//        pdfDoc.close();
//
//    }
//
//    private void convertPdfPageToImage() {
//        String srcPdf = DEST; // 输入的 PDF 文件路径
//        String destPdf = DEST_PIC; // 输出的 PDF 文件路径
//        WriterProperties wp = new WriterProperties();
//        //禁止打印 复制 等操作
//        wp.setStandardEncryption(null, null, EncryptionConstants.ALLOW_ASSEMBLY, EncryptionConstants.DO_NOT_ENCRYPT_METADATA);
//        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(srcPdf), new PdfWriter(destPdf, wp))) {
//            Document document = new Document(pdfDoc);
//            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
//                // 读取 PdfPage 并将其转换为 Image 对象
//                PdfPage page = pdfDoc.getPage(i + 1);
//
//                Image image = new Image(page.copyAsFormXObject(pdfDoc));
//                // 将图像插入到 PDF 中
//                document.add(image);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getPdfInfo() {
//        String srcPdf = "C:\\Users\\leinao\\Documents\\WeChat Files\\wxid_wusvwnjcunzm22\\FileStorage\\File\\2024-02\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见_T.pdf"; // 输入的 PDF 文件路径
//
//        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(srcPdf))) {
//            // 获取 PDF 文件的元数据信息
//            PdfDocumentInfo documentInfo = pdfDoc.getDocumentInfo();
//            // 输出 PDF 文件的页数信息
//            System.out.println(documentInfo);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    protected void manipulatePdf(String dest) throws Exception {
//        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
//        PdfFont font = PdfFontFactory.createFont("STSong-Light", "UniGB-UCS2-H");
//        Paragraph paragraph = new Paragraph("This watermark 飞鸟云")
//                .setFont(font)
//                .setFontSize(15);
//
//        for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
//            PdfPage page = pdfDoc.getPage(i + 1); // iText页面索引从1开始
//
//            // 计算每页的高度，以便平均分布水印
//            float pageWidth = page.getPageSize().getWidth();
//            float pageHeight = page.getPageSize().getHeight();
//            float watermarkWidth = paragraph.getWidth().getValue();
//            float watermarkHeight = paragraph.getHeight().getValue();
//            int numWatermarksPerLine = (int)(pageWidth / watermarkWidth);
//
//            // 计算每行水印的垂直间距
//            float lineHeight = (pageHeight - 3 * watermarkHeight) / 4;
//
//            PdfCanvas over = new PdfCanvas(page);
//            over.setFillColor(ColorConstants.LIGHT_GRAY);
//            over.saveState();
//            PdfExtGState gs1 = new PdfExtGState();
//            gs1.setFillOpacity(0.5f);
//            over.setExtGState(gs1);
//
//            // 在页面上创建三行水印
//            for (int j = 0; j < 3; j++) {
//                float yPosition = pageHeight - (lineHeight * (j + 1)); // 计算每行水印的y坐标
//
//                // 计算每行水印的水平间距
//                float horizontalSpacing = (pageWidth - numWatermarksPerLine * watermarkWidth) / (numWatermarksPerLine + 1);
//
//                // 在每行水印上填充水印
//                for (int k = 0; k < numWatermarksPerLine; k++) {
//                    float xPosition = horizontalSpacing * (k + 1) + k * watermarkWidth; // 计算每个水印的x坐标
//                    Canvas canvasWatermark = new Canvas(over, page.getPageSize())
//                            .showTextAligned(paragraph, xPosition, yPosition, 1,
//                                    TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
//                    canvasWatermark.close();
//                }
//            }
//
//            over.restoreState();
//        }
//
//        pdfDoc.close();
//
//
//    }
}
