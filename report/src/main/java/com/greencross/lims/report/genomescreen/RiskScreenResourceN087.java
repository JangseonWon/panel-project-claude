package com.greencross.lims.report.genomescreen;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public interface RiskScreenResourceN087 extends RiskScreenResource {
	@Override
	default PDImageXObject icon(RiskScreenTemplate.RiskFactor factor) throws IOException {
		File img	= new File(GenomeScreenResource.resource, "img/genomescreen/N087");
		if(factor instanceof GenomeScreenTemplateN087.RiskFactorN087) switch((GenomeScreenTemplateN087.RiskFactorN087)factor) {
			case 고혈압:				return img(new File(img, "riskfactor1.png"));
			case 흡연:				return img(new File(img, "riskfactor2.png"));
			case 비만:				return img(new File(img, "riskfactor3.png"));
			case 당뇨:				return img(new File(img, "riskfactor4.png"));
			case 음주:				return img(new File(img, "riskfactor5.png"));
			case 스트레스:			return img(new File(img, "riskfactor6.png"));
			case 심혈관계_유질환자:	return img(new File(img, "riskfactor7.png"));
			default:				return null;
		}
		return null;
	}
	@Override
	default PDImageXObject[] athleticIcons() throws IOException {
		File img	= new File(GenomeScreenResource.resource, "img/genomescreen/N087");
		return new PDImageXObject[] {
				img(new File(img, "athletic1.png")),
				img(new File(img, "athletic2.png")),
				img(new File(img, "athletic3.png")),
				img(new File(img, "athletic4.png")),
				img(new File(img, "athletic5.png"))
		};
	}
}
