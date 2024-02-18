/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lsy.springcloud;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import net.lingala.zip4j.ZipFile;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

/**
 * Add a diagonal watermark text to each page of a PDF.
 *
 * @author Tilman Hausherr
 */
public class AddWatermarkText
{
    public static final String SRC = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见.pdf";
    public static final String DEST = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见_水印.pdf";
    public static final String DEST_PIC = "D:\\Work\\文档\\20240108_国家发展改革委等部门关于加强高校学生宿舍建设的指导意见_水印_PIC.pdf";

    public static final String ZIP_DEST_PIC = "D:\\Work\\文档\\03三月飞鸟面试PDF资料-原版.zip";
    private AddWatermarkText()
    {
    }

    public static List<File> unzipFile(String path) throws IOException {
        String dir = path.replace(".zip", "");
        if(!cn.hutool.core.io.FileUtil.exist(dir)) {
            cn.hutool.core.io.FileUtil.mkdir(dir);
        }
        ZipFile zipFile = new ZipFile(path);
        zipFile.setCharset(Charset.forName("gbk"));
        zipFile.extractAll(dir);
        List<File> fileList = cn.hutool.core.io.FileUtil.loopFiles(dir, filePath -> cn.hutool.core.io.FileUtil.isFile(filePath) && cn.hutool.core.io.FileUtil.getSuffix(filePath).toLowerCase().equals("pdf"));
        return fileList;
    }
    public static void main(String[] args) throws IOException
    {
        long start = System.currentTimeMillis();

        List<File> files = unzipFile(ZIP_DEST_PIC);
        for (File file : files) {
            handleWaterMaker(file);
        }
        // 打包
        String dstZip = ZIP_DEST_PIC.replace(".zip", "_1.zip");
        String dstPath = ZIP_DEST_PIC.replace(".zip", "");
        ZipFile zipFile = new ZipFile(dstZip);
        zipFile.setCharset(Charset.forName("gbk"));
        zipFile.addFolder(new File(dstPath));
        zipFile.close();
        long stop = System.currentTimeMillis();
        System.out.println((stop - start) + "ms");
    }

    private static void handleWaterMaker(File srcFile) throws IOException {
        String srcPath = srcFile.getCanonicalPath();

        String text = "This watermark 飞鸟云";
        try (PDDocument doc = Loader.loadPDF(srcFile))
        {
            PDType0Font font = PDType0Font.load(doc, new File("C:\\Windows\\Fonts\\simhei.ttf"));
            for (PDPage page : doc.getPages())
            {
                addWatermarkText(doc, page, font, text);
            }
            // 创建新的PDF文档
            PDDocument imageDocument = new PDDocument();
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            for (int pageIndex = 0; pageIndex < doc.getNumberOfPages(); pageIndex++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 渲染页面为图像
                PDPage newPage = new PDPage(new PDRectangle(image.getWidth(), image.getHeight())); // 创建新的页面
                imageDocument.addPage(newPage); // 将新页面添加到新文档中
                // 将图像绘制到新页面
                try (PDPageContentStream contentStream = new PDPageContentStream(imageDocument, newPage, PDPageContentStream.AppendMode.APPEND, true)) {
                    contentStream.drawImage(LosslessFactory.createFromImage(imageDocument, image), 0.0f,0.0f);
                }
            }
            srcFile.delete();
            imageDocument.save(new File(srcPath));
            imageDocument.close();
        }
    }

    private static void addWatermarkText(PDDocument doc, PDPage page, PDFont font, String text)
            throws IOException
    {
        try (PDPageContentStream cs
                = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true))
        {
            float fontHeight = 20; // arbitrary for short text
            float width = page.getMediaBox().getWidth();
            float height = page.getMediaBox().getHeight();
            float stringWidth = font.getStringWidth(text) / 1000 * fontHeight;
            cs.transform(Matrix.getRotateInstance(0, 0, 0));
            cs.setFont(font, fontHeight);
            PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
            gs.setNonStrokingAlphaConstant(0.2f);
            gs.setStrokingAlphaConstant(0.2f);
            gs.setBlendMode(BlendMode.MULTIPLY);
            gs.setLineWidth(3f);
            cs.setGraphicsStateParameters(gs);

            cs.setNonStrokingColor(Color.DARK_GRAY);
            cs.setStrokingColor(Color.DARK_GRAY);
            for (int i =0, h=(int) (height/4); i<3; i++, h+=(height/4)) {
                int count= (int) (width / (stringWidth+10));
                int remBorder = (int) (width - count*(stringWidth+10));
                for (int j = 0; j< count; j++) {
                    cs.beginText();
                    cs.newLineAtOffset(j*(10+stringWidth)+remBorder/2, h);
                    cs.showText(text);
                    cs.endText();
                }

            }


        }
    }

    /**
     * This will print the usage.
     */
    private static void usage()
    {
        System.err.println("Usage: java " + AddWatermarkText.class.getName() + " <input-pdf> <output-pdf> <short text>");
    }
}
