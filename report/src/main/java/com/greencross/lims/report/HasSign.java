package com.greencross.lims.report;

import com.gcgenome.lims.report.Resource;
import com.gcgenome.lims.report.Template;
import com.gcgenome.lims.report.TextStyle;
import com.gcgenome.lims.test.I18N;
import lombok.Value;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

public interface HasSign extends Resource {
	File RESOURCE_DIR = new File("/data/lims/resources");
	Map<String, Person> PERSON_MAP = Map.ofEntries(
			Map.entry("김경미", new Person("김경미", "Kyung-Mi Kim", "M.T.", "50162", new File(RESOURCE_DIR, "/img/sign/enus/김경미.png"))),
			Map.entry("류해인", new Person("류해인", "Hae-In Ryu", "M.T.", "51943", new File(RESOURCE_DIR, "/img/sign/enus/류해인.png"))),
			Map.entry("문예솔", new Person("문예솔", "Ye-Sol Mun", "M.T.", "57130", new File(RESOURCE_DIR, "/img/sign/enus/문예솔.png"))),
			Map.entry("김다솜", new Person("김다솜", "Da-Som Kim", "M.T.", "45102", new File(RESOURCE_DIR, "/img/sign/enus/김다솜.png"))),
			Map.entry("기창석", new Person("기창석", "Chang-Seok Ki", "M.D.", "547", new File(RESOURCE_DIR, "/img/sign/enus/기창석.png"))),
			Map.entry("조은해", new Person("조은해", "Eun-Hae Cho", "M.D.", "690", new File(RESOURCE_DIR, "/img/sign/enus/조은해.png"))),
			Map.entry("송주선", new Person("송주선", "Ju-Seon Song", "M.D.", "997", new File(RESOURCE_DIR, "/img/sign/enus/송주선.png"))),
			Map.entry("설창안", new Person("설창안", "Chang-Ahn Seol", "M.D.", "1037", new File(RESOURCE_DIR, "/img/sign/enus/설창안.png"))),
			Map.entry("이새미", new Person("이새미", "Sae-Mi Lee", "M.D.", "1067", new File(RESOURCE_DIR, "/img/sign/enus/이새미.png"))),
			Map.entry("허주영", new Person("허주영", "Ju-Yeong Heo", "M.D.", "349", new File(RESOURCE_DIR, "/img/sign/enus/허주영.png"))),
			Map.entry("이청화", new Person("이청화", "Cheong-Hwa Lee", "M.D.", "1267", new File(RESOURCE_DIR, "/img/sign/enus/이청화.png")))
	);

	PDFont fontDefault();

	Color colorText();

	default TextStyle stylePerson() {
		return new TextStyle().fonts(fontDefault()).color(colorText()).fontSize(8).paragraph(false);
	}

	default Person person(String name) {
		return PERSON_MAP.get(name);
	}

	SignLabel[] labels();

	static <T extends Template<? extends HasSign>>
	String[][] getSanitizedContributorsLabelsAndNames(T template, I18N i18nSource) {
		return Arrays.stream(template.resource().labels())
				.flatMap(label -> Arrays.stream(label.persons())
						.map(person -> new String[] {
								label.label().replace("/", ", "), label.getLocalizedName(person, i18nSource)})
				).toArray(String[][]::new);
	}

	@Value
	@Accessors(fluent = true)
	class Person {
		String nameKo;
		String nameEn;
		String title;
		private final String license;
		private final File sign;

		public String nameKoWithTitle() {
			return nameKo + " " + title;
		}

		public String nameEnWithTitle() {
			return nameEn + " " + title;
		}
	}

	record SignLabel(String label, Person... persons) {
		public String getLocalizedName(Person person, I18N i18n) {
			return switch (i18n.i18n()) {
				case "ENUS" -> person.nameEn();
				default -> person.nameKo();
			};
		}
	}
}
