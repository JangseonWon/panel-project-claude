package com.gcgenome.lims.client.interpretation;

import com.gcgenome.lims.dto.interpretation.Des;
import com.gcgenome.lims.dto.interpretation.GenomeScreen;
import com.gcgenome.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.PanelTest;
import elemental2.core.JsArray;
import elemental2.core.JsRegExp;
import elemental2.core.RegExpResult;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import net.sayaya.ui.*;
import net.sayaya.ui.chart.Data;
import net.sayaya.ui.chart.SheetElement;
import net.sayaya.ui.chart.SheetElementSelectableMulti;
import net.sayaya.ui.chart.column.ColumnBuilder;
import net.sayaya.ui.chart.column.ColumnText;
import org.jboss.elemento.Elements;
import org.jboss.elemento.HTMLContainerBuilder;
import org.jboss.elemento.IsElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static elemental2.core.Global.JSON;
import static org.jboss.elemento.Elements.div;

public class SelectDiseaseDialog implements IsElement<HTMLDivElement> {
	public static SelectDiseaseDialog instance() {
		return new SelectDiseaseDialog();
	}

	private static final String[] SUFFIXES = new String[]{
			"--없음--",
			"Clinical correlation 및 가족검사가 권장됩니다.",
			"Clinical correlation 및 필요하다고 판단될 경우 가족검사가 권장됩니다.",
			"Clinical correlation이 권장됩니다.",
			"Clinical correlation이 권장되며, 임상적으로 질환 관련성이 높은 유전자에서 한 개의 변이만 발견된 경우 deep intronic variant, large deletion/insertion 등을 검출하기 위한 추가 검사(Diagnostic Genome Sequencing 등)를 고려하시기 바랍니다.",
			"Clinical correlation 및 임상적으로 가장 관련성이 높을 것으로 추정되는 유전자 변이에 대해 가족검사가 권장됩니다.",
			"상염색체 열성 질환에서 한 개의 heterozygous VUS 변이만이 발견된 경우, 1) 이 변이가 질환과 관련이 없는 변이일 가능성, 2) 실제 질환과 관련이 있지만 보인자일 가능성, 3) 실제 이 유전자와 관련된 질환을 가지고 있지만 나머지 하나의 대립 유전자가 본 검사로 검출되지 않는 종류의 변이(large deletion or insertion, deep intronic variant, etc.)를 동반할 가능성 등이 있습니다.",
			"상염색체 열성 질환에서 한 개의 heterozygous PV 만 발견된 경우, 1) 환자가 보인자일 가능성, 2) 환자가 이 유전자와 관련된 질환을 가지고 있지만 나머지 하나의 대립 유전자가 본 검사로 검출되지 않는 종류의 변이(large deletion or insertion, deep intronic variant, etc.)를 동반할 가능성 등이 있습니다.",
	};

	private static ColumnText column(String name) {
		return column(name, name);
	}

	private static ColumnText column(String id, String name) {
		return ColumnBuilder.text(id, 10, 100).name(name).horizontal("center").vertical("middle").readOnly(false);
	}

	private final SheetElement.SheetConfiguration diseaseTableConfig = SheetElement.builder()
			.autoColSize(true)
			.autoRowSize(false)
			.viewportColumnRenderingOffset(100.0)
			.rowHeaderWidth(30)
			.rowHeaders(false)
			.manualColumnMove(false)
			.manualColumnResize(true)
			.columns(
					column("gene", "Gene").build(),
					column("disease", "Disease Name").horizontal("left").build(),
					column("abbr", "Abbreviation").horizontal("left").build(),
					column("inheritance", "Inheritance").build()
			).data(new Data[]{})
			.colWidths(new double[]{50, 120, 50, 30})
			.stretchH("all");
	private final SheetElement diseaseTableSheet = diseaseTableConfig.build();
	private final SheetElement.SheetConfiguration hgvsTableConfig = SheetElement.builder()
			.autoColSize(true)
			.autoRowSize(true)
			.rowHeaderWidth(30)
			.rowHeaders(false)
			.manualColumnMove(false)
			.manualColumnResize(true)
			.data(new Data[] {})
			.heightAuto().stretchH("all")
			.columns(column("Gene").readOnly(true).horizontal("left").build(), column("hgvsc", "HGVS.c").readOnly(true).horizontal("left").build(), column("hgvsp", "HGVS.p").readOnly(true).horizontal("left").build());
	private final SheetElement HgvsTableSheet = hgvsTableConfig.build();
	private final HTMLContainerBuilder<HTMLDivElement> contents = div().style("color: var(--mdc-theme-text-primary-on-background);");
	private final HTMLContainerBuilder<HTMLDivElement> diseaseTable = div().style("width: 800px; height: fit-content; overflow: hidden; border-top: 1px solid #eee; border-bottom: 1px solid #eee;");
	private final HTMLContainerBuilder<HTMLDivElement> hgvsTable = div().style("width: 800px; height: fit-content; overflow: hidden; border-top: 1px solid #eee; border-bottom: 1px solid #eee; margin-top: 32px;");
	private final ButtonElementText cancel = ButtonElement.flat().text("Cancel");
	private final ButtonElementText submit = ButtonElement.contain().text("Apply");
	private final Dialog dialog = Dialog.confirmation("Interpretation parameters", cancel, submit).add(contents);
	private final DropDownElement iptSuffixes;
	private Object previous;
	private final SheetElementSelectableMulti diseaseSelection = SheetElementSelectableMulti.wrap(diseaseTableSheet);
	private final SheetElementSelectableMulti hgvsSelection = SheetElementSelectableMulti.wrap(HgvsTableSheet);

	// private SpreadSheet.SheetBuilder config;
	private Callback<InterpretationParam> callback;

	public SelectDiseaseDialog() {
		ListElement listSuffixex = ListElement.singleLineList();
		for (String suffix : SUFFIXES) {
			ListElement.SingleLineItem li = ListElement.singleLine().label(suffix);
			listSuffixex.add(li);
			li.element().style.setProperty("height", "auto");
			li.element().style.setProperty("padding-top", "5px");
			li.element().style.setProperty("padding-bottom", "5px");

			HTMLElement t = (HTMLElement) li.element().getElementsByClassName("mdc-list-item__text").item(0);
			t.style.whiteSpace = "break-spaces";
			t.style.maxWidth = CSSProperties.MaxWidthUnionType.of("800px");
		}
		iptSuffixes = DropDownElement.outlined(listSuffixex).css("input").text("Suffix").style("width: 800px; margin-top: 10px;");
		HTMLElement ul = listSuffixex.element();
		ul.style.setProperty("font-size", "0.8em");
		HTMLElement menu = (HTMLElement) ul.parentElement;
		menu.style.maxWidth = CSSProperties.MaxWidthUnionType.of("800px");
		contents.add(diseaseTable).add(hgvsTable).add(iptSuffixes);
		cancel.onClick(evt -> dialog.close());
		submit.onClick(evt -> {
			if (callback != null) {
				InterpretationParam param = new InterpretationParam();
				if (diseaseTableConfig != null)
					map(param, Arrays.stream(this.diseaseTableConfig.data()).filter(data -> data.state() == Data.DataState.SELECTED).toArray(Data[]::new));
				if (hgvsTableConfig != null)
					updateVariantsWithHgvs(Arrays.stream(this.hgvsTableConfig.data()).filter(data -> data.state() == Data.DataState.SELECTED).toArray(Data[]::new));
				if (iptSuffixes.selection() > 0) param.suffix(iptSuffixes.value());
				param.previous(previous);
				callback.onSuccess(param);
				try {
					dialog.close();
				} catch (Exception ignore) {
				}
			}

		});
		HTMLElement surface = (HTMLElement) contents.element().parentElement.parentElement;
		surface.style.maxWidth = CSSProperties.MaxWidthUnionType.of("none");
	}

	public SelectDiseaseDialog callback(Callback<InterpretationParam> callback) {
		this.callback = callback;
		return this;
	}

	private InterpretationParam map(InterpretationParam param, Data[] data) {
		for (Data datum : data) {
			String gene = datum.get("gene");
			String disease = datum.get("disease");
			String abbr = datum.get("abbr");
			String inheritance = datum.get("inheritance");
			if (inheritance == null) inheritance = "";
			param.append(gene, new InterpretationParam.InterpretationParamDisease().fullName(disease).abbreviation(abbr).inheritance(inheritance.split(",")));
		}
		return param;
	}

	private void updateVariantsWithHgvs(Data[] data) {
		Arrays.stream(data).forEach(datum ->{
			String snv = datum.get("snv");
			String hgvsc = datum.get("hgvsc");
			String hgvsp = datum.get("hgvsp");
			PreviousObjectUtils.process(previous, new PreviousObjectUtils.Handler<Void>() {
				@Override
				public Void handle(PanelTest p) {
					Stream.concat(Arrays.stream(p.variants()), p.addendum() != null ? Arrays.stream(p.addendum().variants()) : Stream.empty())
							.filter(variant -> snv.equals(variant.snv()))
							.forEach(variant -> {variant.hgvsc(hgvsc); variant.hgvsp(hgvsp);});
					return null;
				}
				@Override
				public Void handle(Des d) {
					Stream.concat(Stream.concat(Arrays.stream(d.variants()), Optional.ofNullable(d.incidentalFindings()).map(Des::variants).map(Arrays::stream).orElse(Stream.empty())),
									Optional.ofNullable(d.incidentalFindings()).map(Des::incidentalFindings).map(Des::variants).map(Arrays::stream).orElse(Stream.empty()))
							.filter(variant -> snv.equals(variant.snv()))
							.forEach(variant -> {variant.hgvsc(hgvsc); variant.hgvsp(hgvsp);});
					return null;
				}      @Override
				public Void handle(GenomeScreen g) {
					Arrays.stream(g.variants())
							.filter(variant -> snv.equals(variant.snv()))
							.forEach(variant -> {variant.hgvsc(hgvsc); variant.hgvsp(hgvsp);});
					return null;
				}
			});
		});
	}

	public SelectDiseaseDialog build(Map<String, String> genes) {
		return build(null, genes, null);
	}

	public SelectDiseaseDialog build(Object previous, Map<String, String> genes, Gene[] predefinedGenes) {
		this.previous = previous;
		diseaseTable.element().innerHTML = "";
		Map<String, List<Gene>> pd = predefinedGenes != null ? Arrays.stream(predefinedGenes).collect(Collectors.groupingBy(g -> g.symbol())) : new HashMap<>();
		if (genes != null && !genes.isEmpty()) {
			int row = 0;
			List<Data> data = new LinkedList<>();
			for (String gene : genes.keySet()) {
				String line = genes.get(gene);
				int selected = 0;
				if (pd.containsKey(gene)) {
					List<Gene> gs = pd.get(gene);
					for (Gene g : gs) {
						data.add(Data.create("").select(true).put("gene", gene).put("disease", g.disease()).put("abbr", g.abbr()).put("inheritance", g.inheritance()));
						row++;
					}
				} else {
					if (line == null) {
						data.add(Data.create("").select(true).put("gene", gene).put("disease", "").put("abbr", "").put("inheritance", ""));
						row++;
						continue;
					}
					if (line.contains("|")) {
						if (line.contains("%")) {
							String[] split = line.split("%");
							String[] diseases = split[0].split("\\|");
							String[] inheritances = ((JsArray<String>) JSON.parse(split[1])).asArray(new String[]{});
							if (inheritances.length == 1 && inheritances[0].contains("|"))
								inheritances = inheritances[0].split("\\|");
							for (int i = 0; i < diseases.length; i++) {
								Data datum = map(gene, diseases[i], inheritances[i]);
								if (selected++ < 2) datum.select(true);
								data.add(datum);
							}
							row += diseases.length;
						} else {
							String[] split = line.split("\\|");
							for (String line2 : split) {
								Data datum = map(gene, line2);
								if (selected++ < 2) datum.select(true);
								data.add(datum);
							}
							row += split.length;
						}
					} else {
						Data datum;
						if (line.contains("%")) {
							String[] split = line.split("%");
							String disease = split[0];
							String[] inheritances = ((JsArray<String>) JSON.parse(split[1])).asArray(new String[]{});
							datum = map(gene, disease, inheritances[0]);
						} else {
							datum = map(gene, line);
						}
						if (selected++ < 2) datum.select(true);
						data.add(datum);
						row++;
					}
				}
			}
			diseaseTable.add(Elements.label().css().textContent("Disease Parameter")).add(diseaseTableSheet);
			diseaseTableSheet.values(data.stream().toArray(Data[]::new));
		}

		hgvsTable.element().innerHTML = "";

		if (hasVariants(previous)) {
			List<Data> data = new ArrayList<>();
			Consumer<PanelTest.Variant[]> processVariants = variants -> {
				Arrays.stream(variants).forEach(variant -> {
					String[] hgvscParts = Optional.ofNullable(variant.originHgvsc())
							.map(s -> s.split("[|,]"))
							.orElse(new String[]{"NM_000000.0:c.?"});
					String[] hgvspParts = Optional.ofNullable(variant.originHgvsp())
							.map(s -> s.split("[|,]"))
							.orElse(new String[0]);
					String defaultHgvsp = hgvspParts.length == 1 ? hgvspParts[0] : "p.?";
					IntStream.range(0, hgvscParts.length)
							.forEach(i -> {
								Data d = map(variant.snv(), variant.gene(), hgvscParts[i],
										i < hgvspParts.length ? hgvspParts[i] : defaultHgvsp);
								data.add(d);
								if (i == 0) d.select(true);
							});
				});
			};

			PreviousObjectUtils.process(previous, new PreviousObjectUtils.Handler<Void>() {
				@Override
				public Void handle(PanelTest p) {
					processVariants.accept(p.variants());
					if (p.addendum() != null) processVariants.accept(p.addendum().variants());
					return null;
				}
				@Override
				public Void handle(Des d) {
					processVariants.accept(d.variants());
					if (d.incidentalFindings() != null) {
						processVariants.accept(d.incidentalFindings().variants());
						if (d.incidentalFindings().incidentalFindings() != null) {
							processVariants.accept(d.incidentalFindings().incidentalFindings().variants());
						}
					}
					return null;
				}
				@Override
				public Void handle(GenomeScreen g) {
					processVariants.accept(g.variants());
					return null;
				}
			});

			hgvsTable.add(Elements.label().css().textContent("HGVS Parameter")).add(HgvsTableSheet);
			HgvsTableSheet.values(data.stream().toArray(Data[]::new));
			HgvsTableSheet.element().style.height = CSSProperties.HeightUnionType.of("auto");
		}
		try {dialog.open();} catch(Exception ignore){}
		return this;
	}
	private static final JsRegExp OMIM_DISEASE_FORMAT = new JsRegExp("^(.+),\\s*\\d+\\s*[(]\\d+[)],*\\s*([A-Za-z ,-]*)$");

	private boolean hasVariants(Object previous) {
		return PreviousObjectUtils.process(previous, new PreviousObjectUtils.Handler<>() {
			@Override
			public Boolean handle(PanelTest p) {
				return (p.variants() != null && p.variants().length > 0)
						|| (p.addendum() != null && p.addendum().variants().length > 0);
			}
			@Override
			public Boolean handle(Des d) {
				return (d.variants() != null && d.variants().length > 0)
						|| (d.incidentalFindings() != null && d.incidentalFindings().variants().length > 0)
						|| (d.incidentalFindings() != null && d.incidentalFindings().incidentalFindings() != null && d.incidentalFindings().incidentalFindings().variants().length > 0);
			}
			@Override
			public Boolean handle(GenomeScreen g) {
				return g.variants() != null && g.variants().length > 0;
			}
		});
	}

	public static Data map(String snv, String gene, String hgvsc, String hgvsp) {
		String dataId = gene + ":" + hgvsc;
		Data data = Data.create(dataId);
		data.put("snv", snv)
				.put("Gene", gene)
				.put("hgvsc", hgvsc)
				.put("hgvsp", hgvsp);
		return data;
	}
	public static Data map(String gene, String line) {
		if(line==null) return Data.create(null);
		line = line.trim();
		Data data = Data.create(line);
		data.put("gene", gene);
		RegExpResult m = OMIM_DISEASE_FORMAT.exec(line);
		String disease, abbr, inheritance;
		if(m!=null) {
			disease = m.getAt(1);
			inheritance = inheritance(m.getAt(2));
		}else {
			disease = line.split(",")[0];
			inheritance = "";
		}
		abbr = diseaseShort(disease);
		data.put("disease", disease);
		data.put("abbr", abbr);
		data.put("inheritance", inheritance);
		return data;
	}

	public static Data map(String gene, String disease, String inheritance) {
		if(disease!=null) disease = disease.trim();
		if(inheritance!=null) inheritance = Arrays.stream(inheritance.split("[,;|]")).map(String::trim).collect(Collectors.joining(";"));
		Data data = Data.create(disease);
		data.put("gene", gene);
		data.put("disease", disease);
		data.put("abbr", diseaseShort(disease));
		if(inheritance != null && !inheritance.isEmpty()) {
			data.put("inheritance", inheritance);
		}
		return data;
	}

	private static final JsRegExp NUMBER_FORMAT = new JsRegExp("^([A-Za-z]{0,1})(\\d*).*$");
	public static String diseaseShort(String fullname) {
		if(fullname == null) return null;
		if(fullname.length()<15) return fullname;
		if(!fullname.contains(" ")) return fullname.substring(0, 4).toUpperCase();
		if(fullname.contains("-")) fullname = fullname.replace("-", " ");
		if(fullname.contains("_")) fullname = fullname.replace("_", " ");
		if(fullname.contains("(")) fullname = fullname.replace("(", " ");
		if(fullname.contains(")")) fullname = fullname.replace(")", "");
		String[] split = fullname.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String s: split) {
			RegExpResult m = NUMBER_FORMAT.exec(s.trim());
			if(m!=null) {
				if(m.getAt(1)!=null) sb.append(m.getAt(1));
				if(m.getAt(2)!=null) sb.append(m.getAt(2));
			} else sb.append(s.charAt(0));
		}
		return sb.toString().toUpperCase();
	}
	public static String inheritance(String orig) {
		if(orig == null) return "";
		orig = orig.trim().toLowerCase();
		List<String> tmp = new LinkedList<>();
		if(orig.contains("autosomal dominant"))	{
			tmp.add("AD");
			orig = orig.replace("autosomal dominant", "");
		}
		if(orig.contains("x-linked dominant")) {
			tmp.add("XLD");
			orig = orig.replace("x-linked dominant", "");
		}
		if(orig.contains("autosomal recessive")){
			tmp.add("AR");
			orig = orig.replace("autosomal recessive", "");
		}
		if(orig.contains("x-linked recessive")) {
			tmp.add("XLR");
			orig = orig.replace("x-linked recessive", "");
		}
		if(orig.contains("digenic dominant")) {
			tmp.add("DD");
			orig = orig.replace("digenic dominant", "");
		}
		if(orig.contains("digenic recessive")) {
			tmp.add("DR");
			orig = orig.replace("digenic recessive", "");
		}
		if(orig.contains("xld")) {
			tmp.add("XLD");
			orig = orig.replace("xld", "");
		}
		if(orig.contains("xlr")) {
			tmp.add("XLR");
			orig = orig.replace("xlr", "");
		}
		if(orig.contains("ad")) {
			tmp.add("AD");
			orig.replace("ad", "");
		}
		if(orig.contains("ar")) {
			tmp.add("AR");
			orig = orig.replace("ar", "");
		}
		if(orig.contains("xd")) {
			tmp.add("XLD");
			orig = orig.replace("xd", "");
		}
		if(orig.contains("xr")) {
			tmp.add("XLR");
			orig = orig.replace("xr", "");
		}
		if(orig.contains("dr")) {
			tmp.add("DR");
			orig = orig.replace("dr", "");
		}
		if(orig.contains("dd")) {
			tmp.add("DD");
			orig = orig.replace("dd", "");
		}
		orig = orig.replace(",", "");
		orig = orig.trim();
		if(!orig.isEmpty()) tmp.add(orig);
		return tmp.stream().distinct().collect(Collectors.joining(", "));
	}
	// Gene Disease Abbr inheritance
	@Override
	public HTMLDivElement element() {
		return dialog.element();
	}
}