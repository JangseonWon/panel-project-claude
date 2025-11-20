package com.greencross.lims.report.genomescreen;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public interface RiskScreenResourceN088 extends RiskScreenResource {
	@Override
	default PDImageXObject icon(RiskScreenTemplate.RiskFactor factor) throws IOException {
		File img	= new File(GenomeScreenResource.resource, "img/genomescreen/N088");
		if(factor instanceof GenomeScreenTemplateN088.RiskFactorN088) switch((GenomeScreenTemplateN088.RiskFactorN088)factor) {
			case 허혈성_뇌졸중:		return img(new File(img, "riskfactor1.png"));
			case 관상동맥:			return img(new File(img, "riskfactor2.png"));
			case 죽상경화성_심혈관질환:	return img(new File(img, "riskfactor3.png"));
			case 암:				return img(new File(img, "riskfactor4.png"));
			default:				return null;
		}
		return null;
	}
	@Override
	default PDImageXObject[] athleticIcons() throws IOException {
		File img	= new File(GenomeScreenResource.resource, "img/genomescreen/N088");
		return new PDImageXObject[] {
				img(new File(img, "athletic1.png")),
				img(new File(img, "athletic2.png")),
				img(new File(img, "athletic3.png")),
				img(new File(img, "athletic4.png")),
				img(new File(img, "athletic5.png"))
		};
	}
}
