package com.greencross.lims.report.builder;

import com.gcgenome.lims.report.TextBlock;
import com.gcgenome.lims.report.func.PDPageContentStreamPageAccessible;
import lombok.experimental.UtilityClass;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.pdmodel.common.function.PDFunctionType2;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.apache.pdfbox.pdmodel.graphics.shading.PDShadingType2;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

@UtilityClass
public class Util {
	public String dashIfEmpty(String str) {
		if(str == null || str.trim().isEmpty()) return "-";
		else return str.trim();
	}
	public void icon(PDPageContentStreamPageAccessible stream, PDImageXObject icon, float x, float y, float HEADER_WIDTH, float HEADER_HEIGHT) throws IOException {
		var scaleHeight = icon.getHeight() * HEADER_WIDTH / icon.getWidth();
		if(scaleHeight <= HEADER_HEIGHT) stream.drawImage(icon, x, y-scaleHeight, HEADER_WIDTH, scaleHeight);
		else {
			var scaleWidth = icon.getWidth() * HEADER_HEIGHT / icon.getHeight();
			stream.drawImage(icon, x, y-HEADER_HEIGHT, scaleWidth, HEADER_HEIGHT);
		}
	}
	public void italic(PDPageContentStreamPageAccessible stream, float x, float y, TextBlock text) throws IOException {
		stream.saveGraphicsState();
		stream.beginText();
		COSArray italicTransformMatrix = new COSArray();
		italicTransformMatrix.add(COSInteger.get(1));
		italicTransformMatrix.add(COSInteger.get(0));
		italicTransformMatrix.add(COSFloat.get("0.2"));
		italicTransformMatrix.add(COSInteger.get(1));
		italicTransformMatrix.add(COSInteger.get(0));
		italicTransformMatrix.add(COSInteger.get(1));
		stream.setNonStrokingColor(text.style().color());
		stream.setTextMatrix(Matrix.concatenate(Matrix.getTranslateInstance(x, y), Matrix.createMatrix(italicTransformMatrix)));
		stream.setFont(text.style().fontMostSuitable(text.text()), text.style().fontSize());
		stream.showText(text.text());
		stream.endText();
		stream.restoreGraphicsState();
	}
	@FunctionalInterface
	public interface DrawFunction {
		void draw(PDPageContentStreamPageAccessible stream) throws IOException;
	}
	public void gradient(PDPageContentStreamPageAccessible stream, DrawFunction func, COSDictionary gradient, int x, int width) throws IOException {
		stream.saveGraphicsState();
		PDShadingType2 axialShading = new PDShadingType2(new COSDictionary());
		axialShading.setColorSpace(PDDeviceRGB.INSTANCE);
		axialShading.setShadingType(PDShading.SHADING_TYPE2);
		COSArray coords1 = new COSArray();
		coords1.add(COSInteger.get(x));
		coords1.add(COSInteger.get(0));
		coords1.add(COSInteger.get(x+width));
		coords1.add(COSInteger.get(0));
		axialShading.setCoords(coords1);
		axialShading.setFunction(new PDFunctionType2(gradient));
		stream.saveGraphicsState();
		func.draw(stream);
		stream.clip();
		stream.shadingFill(axialShading);
		stream.restoreGraphicsState();
	}
}
