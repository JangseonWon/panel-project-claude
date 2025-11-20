package com.greencross.lims.trans;

import com.google.common.base.Splitter;
import com.greencross.lims.entity.Patient;
import lombok.Builder;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class Phrase {
	public String classFull(String acmg) {
		if(acmg == null) return null;
		if(acmg.equals("P") || acmg.equals("PV"))			return "Pathogenic Variant (PV)";
		else if(acmg.equals("LP") || acmg.equals("LPV"))	return "Likely Pathogenic Variant (LPV)";
		else if(acmg.equals("VUS"))							return "Variant of Uncertain Significance (VUS)";
		else if(acmg.equals("B") || acmg.equals("BV"))		return "Benign Variant (BV)";
		else if(acmg.equals("LB") || acmg.equals("LBV"))	return "Likely Benign Variant (LBV)";
		else return "<?>";
	}
	public String classLong(String acmg) {
		if(acmg == null) return null;
		if(acmg.equals("P") || acmg.equals("PV"))			return "Pathogenic Variant";
		else if(acmg.equals("LP") || acmg.equals("LPV"))	return "Likely Pathogenic Variant";
		else if(acmg.equals("VUS"))							return "Variant of Uncertain Significance";
		else if(acmg.equals("B") || acmg.equals("BV"))		return "Benign Variant";
		else if(acmg.equals("LB") || acmg.equals("LBV"))	return "Likely Benign Variant";
		else return "<?>";
	}
	public String classShort(String acmg) {
		if(acmg == null) return null;
		if(acmg.equals("P") || acmg.equals("PV"))			return "PV";
		else if(acmg.equals("LP") || acmg.equals("LPV")) 	return "LPV";
		else if(acmg.equals("VUS"))							return "VUS";
		else if(acmg.equals("B") || acmg.equals("BV"))		return "BV";
		else if(acmg.equals("LB") || acmg.equals("LBV")) 	return "LBV";
		else return "<?>";
	}
	public String inheritanceKor(String inheritance) {
		if(inheritance == null) return null;
		inheritance = inheritance.trim();
		if(inheritance.equals("AD")) 			return "상염색체 우성";
		else if(inheritance.equals("AR")) 		return "상염색체 열성";
		else if(inheritance.equals("XLR")) 		return "성염색체 열성";
		else if(inheritance.equals("XR")) 		return "성염색체 열성";
		else if(inheritance.equals("XLD")) 		return "성염색체 우성";
		else if(inheritance.equals("XD"))		return "성염색체 우성";
		else if(inheritance.equals("XL")) 		return "X-linked";
		else if(inheritance.equals("DD")) 		return "이유전자 우성";
		else if(inheritance.equals("DR")) 		return "이유전자 열성";
		else return "<?>";
	}
	public String inheritanceLong(String inheritance) {
		if(inheritance == null) return null;
		inheritance = inheritance.trim();
		if(inheritance.contains("AD")) 			return "Autosomal dominant";
		else if(inheritance.contains("AR")) 	return "Autosomal recessive";
		else if(inheritance.contains("XLR")) 	return "X-linked recessive";
		else if(inheritance.contains("XR")) 	return "X-linked recessive";
		else if(inheritance.contains("XLD")) 	return "X-linked dominant";
		else if(inheritance.contains("XD")) 	return "X-linked dominant";
		else if(inheritance.contains("XL")) 	return "X-linked";
		else if(inheritance.contains("DD")) 	return "Digenic dominant";
		else if(inheritance.contains("DR")) 	return "Digenic recessive";
		else if(inheritance.contains("SMu"))    return "Somatic mutation";
		else return "<?>";
	}
	public String diseaseName(String fullname) {
		if(fullname == null) return null;
		if(".".equals(fullname.trim())) return null;
		if(fullname.contains("|")) fullname = fullname.substring(0, fullname.indexOf("|"));
		if(fullname.contains(",")) fullname = fullname.substring(0, fullname.indexOf(","));
		fullname = fullname.replace("?", "");
		fullname = fullname.replace("}", "");
		fullname = fullname.replace("{", "");
		return fullname;
	}
	public String diseaseShort(String fullname) {
		if(fullname == null) return null;
		fullname = diseaseName(fullname);
		if(fullname.length()<15) return fullname;
		if(!fullname.contains(" ")) return fullname.substring(0, 4).toUpperCase();
		String[] split = fullname.split(" ");
		StringBuilder sb = new StringBuilder();
		for(String s: split) sb.append(s.charAt(0));
		return sb.toString().toUpperCase();
	}
	public String zygosity(String genotype, Patient.Sex sex, String inheritance) {
		if(genotype == null) return null;
		if("HOM".equalsIgnoreCase(genotype)) return "Homozygous";
		else if("HET".equalsIgnoreCase(genotype)) return "Heterozygous";
		else if("HEM".equalsIgnoreCase(genotype)) return "Hemizygous";

		if(Patient.Sex.M == sex && inheritance.toUpperCase().contains("X")) {
			if("0/1".equals(genotype.trim()))		return "Hemizygous";
			else if("0|1".equals(genotype.trim()))	return "Hemizygous";
			else if("1/0".equals(genotype.trim()))	return "Hemizygous";
			else if("1|0".equals(genotype.trim()))	return "Hemizygous";
			else if("1/1".equals(genotype.trim()))	return "Hemizygous";
			else if("1|1".equals(genotype.trim()))	return "Hemizygous";
			else return null;
		} else if("1/1".equals(genotype.trim()))	return "Homozygous";
		else if("1|1".equals(genotype.trim()))		return "Homozygous";
		else if("0/1".equals(genotype.trim()))		return "Heterozygous";
		else if("0|1".equals(genotype.trim()))		return "Heterozygous";
		else if("1/0".equals(genotype.trim()))		return "Heterozygous";
		else if("1|0".equals(genotype.trim()))		return "Heterozygous";
		else return null;
	}
	public String zygosityShort(String full) {
		if("-".equals(full)) return full;
		if("Homozygous".equals(full)) return "Hom";
		if("Heterozygous".equals(full)) return "Het";
		if("Hemizygous".equals(full)) return "Hem";
		return full;
	}
	public String zygosityFull(String abbr) {
		if("-".equals(abbr)) return abbr;
		if("Hom".equalsIgnoreCase(abbr)) return "Homozygous";
		if("Het".equalsIgnoreCase(abbr)) return "Heterozygous";
		if("Hem".equalsIgnoreCase(abbr)) return "Hemizygous";
		return abbr;
	}
	public int ordinalClass(String acmg) {
		if(acmg == null) return 999;
		if(acmg.equals("P") || acmg.equals("PV"))			return 0;
		else if(acmg.equals("LP") || acmg.equals("LPV"))	return 5;
		else if(acmg.equals("VUS"))							return 9;
		else if(acmg.equals("LB") || acmg.equals("LBV"))	return 15;
		else if(acmg.equals("B") || acmg.equals("BV"))		return 20;
		else return 100;
	}

	@Builder
	private static class PatternComment {
		private final Pattern pattern;
		private final Function<Matcher, String> processor;
		public String comment(String param) {
			Matcher m = pattern.matcher(param);
			if(!m.find()) return null;
			return processor.apply(m);
		}
	}
	private final PatternComment PATTERN_HGVSC1 = PatternComment.builder() //c.123C>T
																.pattern(Pattern.compile("^(\\d+)([A-Z])>([A-Z])"))
																.processor(m->String.format("%s번째 위치한 염기서열 %s가 %s로 치환", m.group(1), m.group(2), m.group(3))).build();
	private final PatternComment PATTERN_HGVSC2 = PatternComment.builder() //c.567+2T>G c.567-3T>G
																.pattern(Pattern.compile("^(\\d+)([+-]{1})(\\d+)([A-Z])>([A-Z])"))
																.processor(m->{
																	String pos = m.group(2).equals("+")?"말단에":"첫 번째";
																	String stream = m.group(2).equals("+")?"downstream":"upstream";
																	return String.format("exon의 %s 위치한 염기인 c.%s로부터 %s 방향으로 %s번째 염기인 %s가 %s로 치환", pos, m.group(1), stream, m.group(3), m.group(4), m.group(5));
																}).build();
	private final PatternComment PATTERN_HGVSC3 = PatternComment.builder() // 6789-6_6789-5dup 6789+6_6789-5del
																.pattern(Pattern.compile("^(\\d+)([+-]{1})(\\d+)\\_(\\d+)([+-]{1})(\\d+)(del|dup)"))
																.processor(m->{
																	String pos1 = m.group(2).equals("+")?"말단에":"첫 번째";
																	String stream1 = m.group(2).equals("+")?"downstream":"upstream";
																	String pos2 = m.group(5).equals("+")?"말단에":"첫 번째";
																	String stream2 = m.group(5).equals("+")?"downstream":"upstream";
																	String deldup = m.group(7).equals("del")?"삭제":"중복";
																	return String.format("exon의 %s 위치한 염기인 c.%s로부터 %s 방향으로 %s번째 염기부터 exon의 %s 위치한 염기인 c.%s로부터 %s 방향으로 %s번째 염기까지 %s",
																						 pos1, m.group(1), stream1, m.group(3), pos2, m.group(4), stream2, m.group(6), deldup);
																}).build();
	private final PatternComment PATTERN_HGVSC4 = PatternComment.builder()
																.pattern(Pattern.compile("^(\\d+)(del|dup)"))
																.processor(m->{
																	String deldup = m.group(2).equals("del")?"삭제":"중복";
																	return String.format("%s번째 위치한 염기가 %s", m.group(1), deldup);
																}).build();
	private final PatternComment PATTERN_HGVSC5 = PatternComment.builder()
																.pattern(Pattern.compile("^(\\d+)\\_(\\d+)(del|dup)"))
																.processor(m->{
																	String deldup = m.group(3).equals("del")?"삭제":"중복";
																	return String.format("%s번째 염기부터 %s번째 염기까지 %s", m.group(1), m.group(2), deldup);
																}).build();
	private final PatternComment PATTERN_HGVSC6 = PatternComment.builder()
																.pattern(Pattern.compile("^(\\d+)\\_(\\d+)ins([A-Z]+)"))
																.processor(m-> String.format("%s번째 염기부터 %s번째 염기 사이에 %s가 삽입", m.group(1), m.group(2), m.group(3))).build();
	private final PatternComment PATTERN_HGVSC7 = PatternComment.builder()
																.pattern(Pattern.compile("^(\\d+)\\_(\\d+)delins([A-Z]+)"))
																.processor(m-> String.format("%s번째 염기부터 %s번째 염기가 삭제되고 %s가 삽입", m.group(1), m.group(2), m.group(3))).build();
	private final PatternComment[] PATTERN_HGVSC = new PatternComment[] {PATTERN_HGVSC1, PATTERN_HGVSC2, PATTERN_HGVSC3, PATTERN_HGVSC4, PATTERN_HGVSC5, PATTERN_HGVSC6, PATTERN_HGVSC7};
	public String hgvscText(String hgvsc){
		if(hgvsc == null || hgvsc.trim().isEmpty()) return null;
		String hgvsc_ = hgvsc.replace("c.","");
		return Arrays.stream(PATTERN_HGVSC).map(c->c.comment(hgvsc_)).filter(Objects::nonNull).findFirst().orElse(null);
	}
	@Builder
	private static final class PatternCommentWithType {
		private final Effect effect;
		private final Pattern pattern;
		private final Function<Matcher, String> processor;
		private final Function<Matcher, String> processorE;
		public String comment(String param) {
			Matcher m = pattern.matcher(param);
			if(!m.find()) return null;
			return processor.apply(m);
		}
		public String commentEnglish(String param) {
			Matcher m = pattern.matcher(param);
			if(!m.find()) return null;
			return processorE.apply(m);
		}
	}
	public enum Effect {
		Nonsense, Missense, StopRetain, StopLoss, Inframe, StartLost, Synonymus,
		Frameshift
	}
	private static final String AMINO_ACID = "[A-Z][a-z]{2}|[A-Z]{1}";
	private final PatternCommentWithType PATTERN_HGVSP10 = PatternCommentWithType.builder().effect(Effect.Nonsense)
																				 .pattern(Pattern.compile("^(AMINO_ACID)(\\d+)(?:Ter|\\*)$".replace("AMINO_ACID", AMINO_ACID)))
																				 .processor(m->{
																					 String aa1 = parseAACodeToName(m.group(1));
																					 if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "이";
																					 else aa1 += "가";
																					 return String.format("%s번째 아미노산인 %s stop codon으로 치환될 것으로 ", m.group(2), aa1);
																				 }).processorE(m->{
																					 String aa1 = parseAACodeToName(m.group(1));
																					 return String.format("This sequence change replaces %s with stop codon at %s amino acid of the {:gene} protein.", aa1, th(Integer.parseInt(m.group(2))));
																				 }).build();
	private final PatternCommentWithType PATTERN_HGVSP11 = PatternCommentWithType.builder().effect(Effect.Missense)
																				 .pattern(Pattern.compile("^(AMINO_ACID)(\\d+)(AMINO_ACID)$".replace("AMINO_ACID", AMINO_ACID)))
																				 .processor(m->{
																					 String aa1 = parseAACodeToName(m.group(1));
																					 if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "이";
																					 else aa1 += "가";
																					 String aa2 = parseAACodeToName(m.group(3));
																					 if(aa2.endsWith("ne") || aa2.endsWith("phan")) aa2 += "으";
																					 return String.format("%s번째 아미노산인 %s %s로 치환될 것으로 ", m.group(2), aa1, aa2);
																				 }).processorE(m->{
																					 String aa1 = parseAACodeToName(m.group(1));
																					 String aa2 = parseAACodeToName(m.group(3));
																					 return String.format("This sequence change replaces %s amino acid %s with %s of the {:gene} protein.", th(Integer.parseInt(m.group(2))), aa1, aa2);
																				 }).build();
	private final PatternCommentWithType PATTERN_HGVSP1 = PatternCommentWithType.builder() // Stop_retain stop retain variant: Ter숫자=
																				.effect(Effect.StopRetain)
																				.pattern(Pattern.compile("^(?:Ter|\\*)(\\d+)\\=$"))
																				.processor(m->String.format("%s번 코돈인 종결 코돈이 그대로 보전될 것으로 ", m.group(1)))
																				.processorE(m->String.format("This sequence change retains stop codon at %s of the {:gene} protein.", th(Integer.parseInt(m.group(1)))))
																				.build();
	private final PatternCommentWithType PATTERN_HGVSP2 = PatternCommentWithType.builder() // stop_lost :  Ter숫자AAextTer[숫자혹은물음표]
																				.effect(Effect.StopLoss)
																				.pattern(Pattern.compile("^(?:Ter|\\*)(\\d+)(AMINO_ACID)ext(Ter|\\*)([0-9?]+)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa = parseAACodeToName(m.group(2));
																					if(aa.endsWith("ne") || aa.endsWith("phan")) aa += "으";
																					return String.format("%s번 코돈인 종결코돈이 %s로 치환되면서 단백질 길이가 연장될 것으로 ", m.group(1), aa);
																				}).processorE(m->{
																					String aa = parseAACodeToName(m.group(2));
																					return String.format("This sequence change extends protein length as it replaces stop codon at %s of the {:gene} with %s.", th(Integer.parseInt(m.group(1))), aa);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP3 = PatternCommentWithType.builder() // AA숫자d
																				.effect(Effect.Inframe)
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)(del|dup)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa = parseAACodeToName(m.group(1));
																					if(aa.endsWith("ne") || aa.endsWith("phan")) aa += "이";
																					else aa += "가";
																					if("del".equalsIgnoreCase(m.group(3))) return String.format("%s번째 아미노산인 %s 제거되면서 단백질 길이가 짧아질 것으로 ", m.group(2), aa);
																					else if("dup".equalsIgnoreCase(m.group(3))) return String.format("%s번째 아미노산인 %s 중복될 것으로 ", m.group(2), aa);
																					else return null;
																				}).processorE(m->{
																					String aa = parseAACodeToName(m.group(1));
																					if("del".equalsIgnoreCase(m.group(3))) return String.format("This sequence change shortened protein length as it deletes %s at %s of the {:gene}.", aa, th(Integer.parseInt(m.group(2))));
																					else if("dup".equalsIgnoreCase(m.group(3))) return String.format("This sequence change duplicates %s amino acid %s of the {:gene}.", aa, th(Integer.parseInt(m.group(2))));
																					else return null;
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP4 = PatternCommentWithType.builder().effect(Effect.Inframe)  // AA숫자_AA숫자del
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)_(AMINO_ACID)(\\d+)(del|dup)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "으";
																					String aa2 = parseAACodeToName(m.group(3));
																					if("del".equalsIgnoreCase(m.group(5))) return String.format("%s번째 아미노산인 %s로부터 %s번째 아미노산인 %s까지 제거되면서 단백질 길이가 짧아질 것으로 ", m.group(2), aa1, m.group(4), aa2);
																					else if("dup".equalsIgnoreCase(m.group(5))) return String.format("%s번째 아미노산인 %s로부터 %s번째 아미노산인 %s까지 한번 더 중복될 것으로 ", m.group(2), aa1, m.group(4), aa2);
																					else return null;
																				}).processorE(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					String aa2 = parseAACodeToName(m.group(3));
																					Integer p1 = Integer.parseInt(m.group(2));
																					Integer p2 = Integer.parseInt(m.group(4));
																					if("del".equalsIgnoreCase(m.group(5))) return String.format("This sequence change shortened protein length by deleting from the %s amino acid %s of the {:gene} to the %s amino acid %s.", th(p1), aa1, th(p2), aa2);
																					else if("dup".equalsIgnoreCase(m.group(5))) return String.format("This sequence change duplicates from %s amino acid %s of the {:gene} to the %s amino acid %s.", th(p1), aa1, th(p2), aa2);
																					else return null;
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP5 = PatternCommentWithType.builder().effect(Effect.Inframe)  //Inframe_delins: AA숫자_AA숫자delinsAA
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)_(AMINO_ACID)(\\d+)delins(AMINO_ACID)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "으";
																					String aa2 = parseAACodeToName(m.group(3));
																					String aa3 = parseAACodeToName(m.group(5));
																					if(aa3.endsWith("ne") || aa3.endsWith("phan")) aa3 += "이";
																					else aa3 += "가";
																					return String.format("%s번째 아미노산인 %s로부터 %s번째 아미노산인 %s까지 제거되고 그 위치에 %s 삽입될 것으로 ", m.group(2), aa1, m.group(4), aa2, aa3);
																				}).processorE(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					String aa2 = parseAACodeToName(m.group(3));
																					String aa3 = parseAACodeToName(m.group(5));
																					Integer p1 = Integer.parseInt(m.group(2));
																					Integer p2 = Integer.parseInt(m.group(4));
																					return String.format("This sequence change replaces the %s amino acid %s through the %s amino acid %s of the {:gene} with %s.", th(p1), aa1, th(p2), aa2, aa3);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP6 = PatternCommentWithType.builder().effect(Effect.Inframe)  //Inframe_dup type3: AA숫자_AA숫자ins[AA여러개]
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)_(AMINO_ACID)(\\d+)ins((AMINO_ACID)+)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "으";
																					String aa2 = parseAACodeToName(m.group(3));
																					String aa3 = Splitter.fixedLength(3)
																										 .splitToList(m.group(5))
																										 .stream()
																										 .map(Phrase::parseAACodeToName)
																										 .collect(Collectors.joining(", "));
																					if(aa3.endsWith("ne") || aa3.endsWith("phan")) aa3 += "이";
																					else aa3 += "가";
																					return String.format("%s번째 아미노산인 %s로부터 %s번째 아미노산인 %s사이에 %s 삽입될 것으로 ", m.group(2), aa1, m.group(4), aa2, aa3);
																				}).processorE(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					String aa2 = parseAACodeToName(m.group(3));
																					String aa3 = parseAACodeToName(m.group(5));
																					Integer p1 = Integer.parseInt(m.group(2));
																					Integer p2 = Integer.parseInt(m.group(4));
																					return String.format("This sequence change inserts %s between the %s amino acid, %s, and the %s amino acid, %s, of the {:gene}.", th(p1), aa3, aa1, th(p2), aa2);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP7 = PatternCommentWithType.builder().effect(Effect.StartLost)
																				.pattern(Pattern.compile("^(AMINO_ACID)1\\?$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa = parseAACodeToName(m.group(1));
																					return String.format("첫번째 아미노산인 %s의 시작코돈이 변경될 것으로 ", aa);
																				}).processorE(m->{
																					String aa = parseAACodeToName(m.group(1));
																					return String.format("This sequence change changes the start codon of %s, the first amino acid of the {:gene} gene.", aa);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP8 = PatternCommentWithType.builder().effect(Effect.Synonymus)
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)=$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa = parseAACodeToName(m.group(1));
																					if(aa.endsWith("ne") || aa.endsWith("phan")) aa += "이";
																					else aa += "가";
																					return String.format("%s번째 아미노산인 %s 그대로 보존될 것으로 ", m.group(2) , aa);
																				}).processorE(m->{
																					String aa = parseAACodeToName(m.group(1));
																					Integer p1 = Integer.parseInt(m.group(2));
																					return String.format("This sequence change preserves %s, the %s amino acid of the {:gene} gene.", aa, th(p1));
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP9 = PatternCommentWithType.builder().effect(Effect.Frameshift)
																				.pattern(Pattern.compile("^(AMINO_ACID)(\\d+)(AMINO_ACID)fs(Ter|\\*)*(\\d+)$".replace("AMINO_ACID", AMINO_ACID)))
																				.processor(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "이";
																					else aa1 += "가";
																					String aa2 = parseAACodeToName(m.group(3));
																					if(aa2.endsWith("ne") || aa2.endsWith("phan")) aa2 += "으";
																					return String.format("%s번째 아미노산인 %s %s로 치환되고 해석 틀이 변하여 그 후로 %s번째 오는 아미노산이 stop codon으로 변경될 것으로 ", m.group(2), aa1, aa2, m.group(5));
																				}).processorE(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					String aa2 = parseAACodeToName(m.group(3));
																					Integer p1 = Integer.parseInt(m.group(2));
																					Integer p2 = Integer.parseInt(m.group(5));
																					return String.format("This variant changes reading frame of the {:gene} gene and the %s amino acid to a stop codon due to the %s amino acid, %s, is substituted with %s.", th(p2), th(p1), aa1, aa2);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP90 = PatternCommentWithType.builder().effect(Effect.Frameshift)
																				 .pattern(Pattern.compile("^(?:Ter|\\*)(\\d+)(AMINO_ACID)fs(Ter|\\*)*(\\d+)$".replace("AMINO_ACID", AMINO_ACID)))
																				 .processor(m->{
																					 String aa2 = parseAACodeToName(m.group(2));
																					 if(aa2.endsWith("ne") || aa2.endsWith("phan")) aa2 += "으";
																					 return String.format("%s번째 Stop codon이 %s로 치환되고 해석 틀이 변하여 그 후로 %s번째 오는 아미노산이 stop codon으로 변경될 것으로 ", m.group(1), aa2, m.group(4));
																				 }).processorE(m->{
																					String aa2 = parseAACodeToName(m.group(2));
																					Integer p1 = Integer.parseInt(m.group(1));
																					Integer p2 = Integer.parseInt(m.group(4));
																					return String.format("This variant changes reading frame of the {:gene} gene and the %s amino acid to a stop codon due to the %s stop codon is substituted with %s.", th(p2), th(p1), aa2);
																				}).build();
	private final PatternCommentWithType PATTERN_HGVSP91 = PatternCommentWithType.builder().effect(Effect.Frameshift)
																				 .pattern(Pattern.compile("^(AMINO_ACID)(\\d+)(AMINO_ACID)fs(Ter|\\*)*$".replace("AMINO_ACID", AMINO_ACID)))
																				 .processor(m->{
																					 String aa1 = parseAACodeToName(m.group(1));
																					 if(aa1.endsWith("ne") || aa1.endsWith("phan")) aa1 += "이";
																					 else aa1 += "가";
																					 String aa2 = parseAACodeToName(m.group(3));
																					 if(aa2.endsWith("ne") || aa2.endsWith("phan")) aa2 += "으";
																					 return String.format("%s번째 아미노산인 %s %s로 치환되고 해석 틀이 변하여 다음 아미노산이 stop codon으로 변경될 것으로 ", m.group(2), aa1, aa2);
																				 }).processorE(m->{
																					String aa1 = parseAACodeToName(m.group(1));
																					String aa2 = parseAACodeToName(m.group(3));
																					Integer p1 = Integer.parseInt(m.group(2));
																					return String.format("This variant changes reading frame of the {:gene} gene due to the %s amino acid %s is substituted with %s and followed amino acid to a stop codon.", th(p1), aa1, aa2);
																				}).build();
	private final PatternCommentWithType[] PATTERN_HGVSP = new PatternCommentWithType[] {
		PATTERN_HGVSP10, PATTERN_HGVSP11, PATTERN_HGVSP1, PATTERN_HGVSP2, PATTERN_HGVSP3, PATTERN_HGVSP4, PATTERN_HGVSP5, PATTERN_HGVSP6,
		PATTERN_HGVSP7, PATTERN_HGVSP8, PATTERN_HGVSP90, PATTERN_HGVSP9, PATTERN_HGVSP91, PATTERN_HGVSP9
	};
	private String th(int n) {
		if(n <= 0) return String.valueOf(n);
		if(n == 1) return "1st";
		if(n == 2) return "2nd";
		if(n == 3) return "3rd";
		else return n + "th";
	}
	public String hgvspText(String hgvsp){
		if(hgvsp == null || hgvsp.trim().isEmpty()) return null;
		String hgvsp_ = hgvsp.replace("p.","");
		if(hgvsp.trim().isEmpty() || "(?)".equalsIgnoreCase(hgvsp)) return null;
		return Arrays.stream(PATTERN_HGVSP).map(p->p.comment(hgvsp_)).filter(Objects::nonNull).findFirst().orElse(null);
	}
	public String effect(String hgvsp){
		if(hgvsp == null || hgvsp.trim().isEmpty()) return null;
		String hgvsp_ = hgvsp.replace("p.","");
		if(hgvsp.trim().isEmpty() || "(?)".equalsIgnoreCase(hgvsp)) return null;
		return Arrays.stream(PATTERN_HGVSP).map(p->p.comment(hgvsp_)).filter(Objects::nonNull).findFirst().orElse(null);
	}
	public String hgvspTextEnglish(String gene, String hgvsp) {
		if(hgvsp == null || hgvsp.trim().isEmpty()) return null;
		String hgvsp_ = hgvsp.replace("p.","");
		if(hgvsp.trim().isEmpty() || "(?)".equalsIgnoreCase(hgvsp)) return null;
		return Arrays.stream(PATTERN_HGVSP).map(p->p.commentEnglish(hgvsp_)).filter(Objects::nonNull).map(p->p.replace("{:gene}", gene)).findFirst().orElse(null);
	}
	private String parseAACodeToName(String code) {
		if(code.length() == 1) return parseAACodeToName(parseAACodeTo3Digit(code));
		switch(code) {
			case "Ala": return "Alanine";
			case "Arg": return "Arginine";
			case "Asn": return "Asparagine";
			case "Asp": return "Aspartic acid";
			case "Cys": return "Cysteine";
			case "Glu": return "Glutamic acid";
			case "Gln": return "Glutamine";
			case "Gly": return "Glycine";
			case "His": return "Histidine";
			case "Ile": return "Isoleucine";
			case "Leu": return "Leucine";
			case "Lys": return "Lysine";
			case "Met": return "Methionine";
			case "Phe": return "Phenylalanine";
			case "Pro": return "Proline";
			case "Ser": return "Serine";
			case "Thr": return "Threonine";
			case "Trp": return "Tryptophan";
			case "Tyr": return "Tyrosine";
			case "Val": return "Valine";
		}
		return null;
	}
	private String parseAACodeTo3Digit(String c) {
		switch(c) {
			case "A": return "Ala";
			case "R": return "Arg";
			case "N": return "Asn";
			case "D": return "Asp";
			case "C": return "Cys";
			case "E": return "Glu";
			case "Q": return "Gln";
			case "G": return "Gly";
			case "H": return "His";
			case "I": return "Ile";
			case "L": return "Leu";
			case "K": return "Lys";
			case "M": return "Met";
			case "F": return "Phe";
			case "P": return "Pro";
			case "S": return "Ser";
			case "T": return "Thr";
			case "W": return "Trp";
			case "Y": return "Tyr";
			case "V": return "Val";
			case "*": return "Ter";
		}
		return null;
	}
	private String parseAACodeTo1Digit(String c) {
		switch(c) {
			case "Ala": return "A";
			case "Arg": return "R";
			case "Asn": return "N";
			case "Asp": return "D";
			case "Cys": return "C";
			case "Glu": return "E";
			case "Gln": return "Q";
			case "Gly": return "G";
			case "His": return "H";
			case "Ile": return "I";
			case "Leu": return "L";
			case "Lys": return "K";
			case "Met": return "M";
			case "Phe": return "F";
			case "Pro": return "P";
			case "Ser": return "S";
			case "Thr": return "T";
			case "Trp": return "W";
			case "Tyr": return "Y";
			case "Val": return "V";
			case "Ter": return "*";
		}
		return null;
	}
}
