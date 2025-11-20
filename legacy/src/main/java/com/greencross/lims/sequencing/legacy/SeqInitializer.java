package com.greencross.lims.sequencing.legacy;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Batch;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class SeqInitializer {
    private final Pattern LIBRARY_ID_PATTERN = Pattern.compile("^(\\d{0,2})([가-힣A-Za-z_-]+)(\\d+)");
    private final UUID LIBRARYQC_SAMPLE_BATCH_NUM   = UUID.fromString("82de070b-bce1-4263-bf86-77e6daaa9034");
    private final UUID LIBRARYQC_BATCH_NUM          = UUID.fromString("385be449-6f49-4d9f-873e-0ab30850776b");
    private final UUID LIBRARYQC_PATIENT_NAME       = UUID.fromString("5e5e7085-c856-4daa-a866-76ec1218eacf");
    private final UUID SEQUENCING_ANALYSIS_NAME     = UUID.fromString("f46136d7-7cfc-4f79-adb0-c254edd5c72a");
    public void assignSeqName(Batch<?> parent) {
        Map<String, Integer> panelIdx = Maps.newHashMap();
        Set<String> items = Sets.newHashSet();
        for(Analysis<?> child: parent.analysis()) {
            String code = child.service() != null ? child.service().id() : null;
            Matcher m = LIBRARY_ID_PATTERN.matcher(child.serial());
            if (m.find()) {
                String year = m.group(1);
                if (year == null || year.isEmpty()) {        // For TNBC
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yy");
                    year = sdf2.format(new Date());
                }
                String batchNum = child.value().get(LIBRARYQC_SAMPLE_BATCH_NUM);
                if (batchNum == null) batchNum = parent.value().get(LIBRARYQC_BATCH_NUM);
                if (batchNum != null) batchNum = String.format("%03d", Integer.parseInt(batchNum.trim()));
                System.out.println(code);
                if(mapBiWesCodeToPanelType(code)!=null) {
                    String code1 = mapBiWesCodeToPanelType(code)[0];
                    String code2 = mapBiWesCodeToPanelType(code)[1];
                    String prefix = year + code1 + batchNum;
                    if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                    panelIdx.put(code1, panelIdx.get(code1) + 1);
                    if (child.sample() != null) {
                        String pid = String.valueOf(child.sample().id());
                        child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" + pid.substring(0, 8) + "-" + pid.substring(8, 11) + "-" + pid.substring(11) + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_I");
                    } else if (child.value().get(LIBRARYQC_PATIENT_NAME) != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" +  child.value().get(LIBRARYQC_PATIENT_NAME).replace(" ", "_") + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_I");
                    else if (child.serial() != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" +  child.serial().replace(" ", "_") + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_I");
                    else child.value().put(SEQUENCING_ANALYSIS_NAME,  prefix + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_I");
                    items.add(year + code1 + batchNum);
                } else if(mapTsoCodeToPanelType(code)!=null) {
                    String code1 = mapTsoCodeToPanelType(code)[0];
                    String code2 = mapTsoCodeToPanelType(code)[1];
                    String prefix = year + code1 + batchNum;
                    if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                    panelIdx.put(code1, panelIdx.get(code1) + 1);
                    if (child.sample() != null) {
                        String pid = String.valueOf(child.sample().id());
                        child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "-" + String.format("%02d", panelIdx.get(code1)) + "_" + pid.substring(0, 8) + "-" + pid.substring(8, 11) + "-" + pid.substring(11) + "_" + code2 + "_I");
                    } else if (child.value().get(LIBRARYQC_PATIENT_NAME) != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "-" + String.format("%02d", panelIdx.get(code1)) + "_" +  child.value().get(LIBRARYQC_PATIENT_NAME).replace(" ", "_") + "_" + code2 + "_I");
                    else if (child.serial() != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "-" + String.format("%02d", panelIdx.get(code1)) + "_" +  child.serial().replace(" ", "_") + "_" + code2 + "_I");
                    else child.value().put(SEQUENCING_ANALYSIS_NAME,  prefix + "-" + String.format("%02d", panelIdx.get(code1)) + "_" + code2 + "_I");
                    items.add(year + code1 + batchNum);
                } else if(mapCodeToPanelType(code)!=null){
                    String code1 = mapCodeToPanelType(code)[0];
                    String code2 = mapCodeToPanelType(code)[1];
                    String prefix = year + code1 + batchNum;
                    if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                    panelIdx.put(code1, panelIdx.get(code1) + 1);
                    if (child.sample() != null) {
                        String pid = String.valueOf(child.sample().id());
                        child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) +"_" + pid.substring(0, 8) + "-" + pid.substring(8, 11) + "-" + pid.substring(11));
                    } else if (child.value().get(LIBRARYQC_PATIENT_NAME) != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" +  code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_" + child.value().get(LIBRARYQC_PATIENT_NAME).replace(" ", "_"));
                    else if (child.serial() != null) child.value().put(SEQUENCING_ANALYSIS_NAME, prefix + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)) + "_" + child.serial().replace(" ", "_"));
                    else child.value().put(SEQUENCING_ANALYSIS_NAME,  prefix + "_" + code2 + "-" + String.format("%02d", panelIdx.get(code1)));
                    items.add(year + code1 + batchNum);
                } else {
                    String serial = null;
                    if ("N114".equalsIgnoreCase(code)   // DGS BI Only
                            || "A005".equalsIgnoreCase(code)   // DGS plus(Proband)
                            || "A006".equalsIgnoreCase(code)   // DGS plus Trio(부)
                            || "A007".equalsIgnoreCase(code)   // DGS plus Trio(모)
                            || "A008".equalsIgnoreCase(code)   // DGS plus + DMS(Proband)
                            || "A009".equalsIgnoreCase(code)   // DGS plus + DMS(부)
                            || "A010".equalsIgnoreCase(code)) {// DGS plus + DMS(모)
                        // 20DES001BI-01_20200205-171-5100
                        String code1 = toCode(code)[0];
                        String code2 = toCode(code)[1];
                        String prefix = year + code1 + batchNum + (code2 != null ? code2 : "") + "-";
                        if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                        panelIdx.put(code1, panelIdx.get(code1) + 1);
                        serial = prefix + String.format("%02d", panelIdx.get(code1));
                        items.add(year + code1 + batchNum);
                    } else if ("T015".equalsIgnoreCase(code)) {
                        // 20WES001-001_20200205-171-5100
                        String code1 = "WES";
                        String prefix = year + code1 + batchNum + "-";
                        if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                        panelIdx.put(code1, panelIdx.get(code1) + 1);
                        serial = prefix + String.format("%02d", panelIdx.get(code1));
                        items.add(year + code1 + batchNum);
                    } else if ("T019".equals(code)) {
                        String code1 = "Study";
                        String prefix = year + code1 + batchNum + "-";
                        if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                        panelIdx.put(code1, panelIdx.get(code1) + 1);
                        serial = prefix + String.format("%02d", panelIdx.get(code1));
                        items.add(year + code1 + batchNum);
                    } else if (code != null) {
                        // I_20Cardio1_CM-10_20200203-171-5176
                        String code1 = toCode(code)[0];
                        String code2 = toCode(code)[1];
                        String prefix = "I_" + year + code1 + batchNum + (code2 != null ? "_" + code2 : "") + "-";
                        if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                        panelIdx.put(code1, panelIdx.get(code1) + 1);
                        serial = prefix + String.format("%02d", panelIdx.get(code1));
                        items.add(year + code1 + batchNum);
                    } else {
                        String code1 = m.group(2);
                        if ("Cancer".equalsIgnoreCase(code1)) {
                            String prefix = year + code1 + batchNum + "-";
                            if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                            panelIdx.put(code1, panelIdx.get(code1) + 1);
                            serial = prefix + String.format("%02d", panelIdx.get(code1));
                        } else {
                            String prefix = "I_" + year + code1 + batchNum + "-";
                            if (!panelIdx.containsKey(code1)) panelIdx.put(code1, 0);
                            panelIdx.put(code1, panelIdx.get(code1) + 1);
                            serial = prefix + String.format("%02d", panelIdx.get(code1));
                        }
                        items.add(year + code1 + batchNum);
                    }
                    if (child.sample() != null) {
                        String pid = String.valueOf(child.sample().id());
                        child.value().put(SEQUENCING_ANALYSIS_NAME, serial + "_" + pid.substring(0, 8) + "-" + pid.substring(8, 11) + "-" + pid.substring(11));
                    } else if (child.value().get(LIBRARYQC_PATIENT_NAME) != null) {
                        child.value().put(SEQUENCING_ANALYSIS_NAME, serial + "_" + child.value().get(LIBRARYQC_PATIENT_NAME).replace(" ", "_"));
                    } else if (child.serial() != null) {
                        child.value().put(SEQUENCING_ANALYSIS_NAME, serial + "_" + child.serial().replace(" ", "_"));
                    } else child.value().put(SEQUENCING_ANALYSIS_NAME, serial.replace(" ", "_"));
                }
            } else if ("T022".equalsIgnoreCase(code)) {
                if(child.sample()!=null) {
                    String pid = String.valueOf(child.sample().id());
                    child.value().put(SEQUENCING_ANALYSIS_NAME, child.serial() + "_GC_" + pid.substring(0, 8));
                } else child.value().put(SEQUENCING_ANALYSIS_NAME, child.serial() + "_GC");
            }
        }
        String title = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "_" + String.join("_", items);
        Map<UUID, String> values = parent.value();
        values.put(UUID.fromString("31c18e1d-2b37-431f-9e88-0cd59d964373"), "Enrichment");	// Workflow
        values.put(UUID.fromString("dcf8f21f-231a-496e-a8da-f748a24c951c"), "Enrichment");	// Application
        values.put(UUID.fromString("0c8eaf97-62a7-4de8-84e3-caa902ca4e76"), "151");			// Read1
        values.put(UUID.fromString("572f4c04-487c-4582-af46-fee1b9b20d5f"), "151");			// Read2
        values.put(UUID.fromString("3fffff88-05ee-4792-a931-779c523f7526"), "8");			// Index1
        values.put(UUID.fromString("fc312d65-e14a-475b-a04a-dcbdb66e60e0"), "8");			// Index2
        values.put(UUID.fromString("3bae5a4f-24e6-48a4-9f48-4c89e6b25936"), "0");			// Reverse Complement
        values.put(UUID.fromString("753b2790-d1e7-4d33-aa93-3eaaa99ed5a8"), "1");			// FlagPCRDuplicates
        values.put(UUID.fromString("3f2f87ab-0e8c-4ce6-bc5b-9a0f3f72fd97"), "TruSeq HT");	// Assay
        values.put(UUID.fromString("36f35add-6bfe-43ed-bca2-5a2d1bc87556"), "Amplicon");	// Chemistry
        parent.title(title);
    }

    // 22AN001_20220101-111-0000_F9-01_I 의 형태로 생성되는 코드
    private String[] mapBiWesCodeToPanelType (String code) {
        if("N141".equals(code)) return new String[] {"WES", "WES"};
        else if("ON141".equals(code)) return new String[] {"WES", "WES"};
        else if("T016".equals(code)) return new String[] {"WES", "WES"};
        else return null;
    }
    // 23STTSO003-01_20231107-171-5013_ST_I 의 형태로 생성되는 코드
    private String[] mapTsoCodeToPanelType (String code) {
        if("N198".equals(code)) return new String[] {"CFTSO", "CF"};
        else if("N199".equals(code)) return new String[] {"STTSO", "ST"};
        else if("N200".equals(code)) return new String[] {"STTSO", "ST"};
        else if("ON198".equals(code)) return new String[] {"CFTSO", "CF"};
        else if("ON199".equals(code)) return new String[] {"STTSO", "ST"};
        else if("ON200".equals(code)) return new String[] {"STTSO", "ST"};
        else if("G0022402".equals(code)) return new String[] {"STTSO", "ST"};
        else return null;
    }

    // 25WES018_EPIL-25_20250124-171-5034 의 형태로 생성되는 코드
    private String[] mapCodeToPanelType (String code) {
        if("G068".equals(code)) return new String[] {"BRCA", "BRCA"};
        else if("G069".equals(code)) return new String[] {"BRCA", "BRCA"};
        else if("N101".equals(code)) return new String[] {"BRCA", "BRCA"};
        else if("N111".equals(code)) return new String[] {"BRCA", "BRCA"};
        else if("X009".equals(code)) return new String[] {"BRCA", "TP53"};
        else if("Z137".equals(code)) return new String[] {"BRCA", "BRCA"};
        else if("Z138".equals(code)) return new String[] {"BRCA", "BRCA"};

        else if("N040".equals(code)) return new String[] {"Cancer", "canP"};
        else if("N074".equals(code)) return new String[] {"Cancer", "SMC"};
        else if("N076".equals(code)) return new String[] {"Cancer", "can"};

        else if("ON090".equals(code)) return new String[] {"Cancer", "GSC"};
        else if("N257".equals(code)) return new String[] {"Cancer", "NF1"};
        else if("S061".equals(code)) return new String[] {"Cancer", "MEN1"};
        else if("S062".equals(code)) return new String[] {"Cancer", "NF2"};
        else if("S063".equals(code)) return new String[] {"Cancer", "PTEN"};
        else if("S096".equals(code)) return new String[] {"Cancer", "GBRCA"};
        else if("S097".equals(code)) return new String[] {"Cancer", "GBRCA"};
        else if("S128".equals(code)) return new String[] {"Cancer", "TSC1"};
        else if("S129".equals(code)) return new String[] {"Cancer", "TSC2"};
        else if("Z141".equals(code)) return new String[] {"Cancer", "MLH1L"};
        else if("Z962".equals(code)) return new String[] {"Cancer", "MSH2L"};
        else if("Z964".equals(code)) return new String[] {"Cancer", "APCL"};
        else if("N090".equals(code)) return new String[] {"Cancer", "GSC"};
        else if("J018".equals(code)) return new String[] {"Cancer", "GSC"};

        else if("N027".equals(code)) return new String[] {"WES", "SLC26A4"};
        else if("N037".equals(code)) return new String[] {"WES", "ARH"};
        else if("N038".equals(code)) return new String[] {"WES", "CM"};
        else if("N039".equals(code)) return new String[] {"WES", "DER"};
        else if("N041".equals(code)) return new String[] {"WES", "SD"};
        else if("N042".equals(code)) return new String[] {"WES", "SS"};
        else if("N043".equals(code)) return new String[] {"WES", "CTD"};
        else if("N044".equals(code)) return new String[] {"WES", "MD"};
        else if("N045".equals(code)) return new String[] {"WES", "MP"};
        else if("N046".equals(code)) return new String[] {"WES", "ATX"};
        else if("N047".equals(code)) return new String[] {"WES", "SPG"};
        else if("N048".equals(code)) return new String[] {"WES", "DSD"};
        else if("N049".equals(code)) return new String[] {"WES", "LSD"};
        else if("N050".equals(code)) return new String[] {"WES", "IEM"};
        else if("N051".equals(code)) return new String[] {"WES", "RAS"};
        else if("N052".equals(code)) return new String[] {"WES", "CH"};
        else if("N053".equals(code)) return new String[] {"WES", "COAG"};
        else if("N054".equals(code)) return new String[] {"WES", "EPIL"};
        else if("N055".equals(code)) return new String[] {"WES", "CMT"};
        else if("N057".equals(code)) return new String[] {"WES", "WES"};
        else if("N058".equals(code)) return new String[] {"WES", "WES"};
        else if("N059".equals(code)) return new String[] {"WES", "WES"};
        else if("N062".equals(code)) return new String[] {"WES", "PID"};
        else if("N067".equals(code)) return new String[] {"WES", "IGD"};
        else if("N071".equals(code)) return new String[] {"WES", "RP"};
        else if("N072".equals(code)) return new String[] {"WES", "EYE"};
        else if("N073".equals(code)) return new String[] {"WES", "HL"};
        else if("N078".equals(code)) return new String[] {"WES", "PKS"};
        else if("N079".equals(code)) return new String[] {"WES", "ALZ"};
        else if("N080".equals(code)) return new String[] {"WES", "DEM"};
        else if("N081".equals(code)) return new String[] {"WES", "MODY"};
        else if("N084".equals(code)) return new String[] {"WES", "DYT"};
        else if("N095".equals(code)) return new String[] {"WES", "AHUS"};
        else if("N096".equals(code)) return new String[] {"WES", "ALP"};
        else if("N097".equals(code)) return new String[] {"WES", "NPHP"};
        else if("N098".equals(code)) return new String[] {"WES", "PKD"};
        else if("N099".equals(code)) return new String[] {"WES", "HPL"};
        else if("N100".equals(code)) return new String[] {"WES", "STK"};
        else if("N106".equals(code)) return new String[] {"WES", "MCPH"};
        else if("N107".equals(code)) return new String[] {"WES", "AUT"};
        else if("N108".equals(code)) return new String[] {"WES", "PCD"};
        else if("N110".equals(code)) return new String[] {"WES", "ND"};
        else if("N119".equals(code)) return new String[] {"WES", "NET"};
        else if("N121".equals(code)) return new String[] {"WES", "LDLR"};
        else if("N122".equals(code)) return new String[] {"WES", "MT"};
        else if("ON122".equals(code)) return new String[] {"WES", "MT"};
        else if("N123".equals(code)) return new String[] {"WES", "CHOL"};
        else if("N130".equals(code)) return new String[] {"WES", "ALS"};
        else if("N142".equals(code)) return new String[] {"WES", "WES"};
        else if("N148".equals(code)) return new String[] {"WES", "ANE"};
        else if("N149".equals(code)) return new String[] {"WES", "IBD"};
        else if("N176".equals(code)) return new String[] {"WES", "ALSW"};
        else if("N180".equals(code)) return new String[] {"WES", "CMG"};
        else if("N181".equals(code)) return new String[] {"WES", "ITP"};
        else if("N182".equals(code)) return new String[] {"WES", "SCN"};
        else if("N183".equals(code)) return new String[] {"WES", "IBMFS"};
        else if("N184".equals(code)) return new String[] {"WES", "HLH"};
        else if("N187".equals(code)) return new String[] {"WES", "SMCP"};
        else if("N188".equals(code)) return new String[] {"WES", "MOC"};
        else if("N189".equals(code)) return new String[] {"WES", "PRF"};
        else if("N190".equals(code)) return new String[] {"WES", "CAKUT"};
        else if("N191".equals(code)) return new String[] {"WES", "HHC"};
        else if("N192".equals(code)) return new String[] {"WES", "PDD"};
        else if("N193".equals(code)) return new String[] {"WES", "OCA"};
        else if("N194".equals(code)) return new String[] {"WES", "OA"};
        else if("N208".equals(code)) return new String[] {"WES", "WES"};
        else if("N209".equals(code)) return new String[] {"WES", "WES"};
        else if("N210".equals(code)) return new String[] {"WES", "FA"};
        else if("N211".equals(code)) return new String[] {"WES", "MO"};
        else if("N214".equals(code)) return new String[] {"WES", "RHAB"};
        else if("N215".equals(code)) return new String[] {"WES", "LEUK"};
        else if("N216".equals(code)) return new String[] {"WES", "ARTH"};
        else if("N217".equals(code)) return new String[] {"WES", "CIL"};
        else if("N218".equals(code)) return new String[] {"WES", "OGS"};
        else if("N219".equals(code)) return new String[] {"WES", "CAT"};
        else if("N220".equals(code)) return new String[] {"WES", "CMAL"};
        else if("N221".equals(code)) return new String[] {"WES", "CVM"};
        else if("N222".equals(code)) return new String[] {"WES", "CPL"};
        else if("N223".equals(code)) return new String[] {"WES", "PFIB"};
        else if("N224".equals(code)) return new String[] {"WES", "PAH"};
        else if("N225".equals(code)) return new String[] {"WES", "DIAR"};
        else if("N226".equals(code)) return new String[] {"WES", "GLC"};
        else if("N227".equals(code)) return new String[] {"WES", "ERY"};
        else if("N228".equals(code)) return new String[] {"WES", "HT"};
        else if("N229".equals(code)) return new String[] {"WES", "AMYL"};
        else if("N230".equals(code)) return new String[] {"WES", "HYDC"};
        else if("N231".equals(code)) return new String[] {"WES", "HTH"};
        else if("N232".equals(code)) return new String[] {"WES", "NYST"};
        else if("N233".equals(code)) return new String[] {"WES", "ICC"};
        else if("N234".equals(code)) return new String[] {"WES", "IRON"};
        else if("N235".equals(code)) return new String[] {"WES", "HTS"};
        else if("N236".equals(code)) return new String[] {"WES", "MCD"};
        else if("N237".equals(code)) return new String[] {"WES", "MHT"};
        else if("N238".equals(code)) return new String[] {"WES", "OSP"};
        else if("N239".equals(code)) return new String[] {"WES", "PS"};
        else if("N240".equals(code)) return new String[] {"WES", "LYMP"};
        else if("N241".equals(code)) return new String[] {"WES", "POI"};
        else if("N242".equals(code)) return new String[] {"WES", "NPHS"};
        else if("N243".equals(code)) return new String[] {"WES", "TUB"};
        else if("N244".equals(code)) return new String[] {"WES", "OBS"};
        else if("N245".equals(code)) return new String[] {"WES", "TAAD"};
        else if("N246".equals(code)) return new String[] {"WES", "DIN"};
        else if("N247".equals(code)) return new String[] {"WES", "IPO"};
        else if("N248".equals(code)) return new String[] {"WES", "CHD"};
        else if("N249".equals(code)) return new String[] {"WES", "IMPD"};
        else if("N250".equals(code)) return new String[] {"WES", "CSO"};
        else if("ON142".equals(code)) return new String[] {"WES", "WES"};
        else if("ON201".equals(code)) return new String[] {"WES", "WES"};
        else if("ON209".equals(code)) return new String[] {"WES", "WES"};
        else if("ON210".equals(code)) return new String[] {"WES", "FA"};
        else if("ON211".equals(code)) return new String[] {"WES", "MO"};
        else if("R2200201".equals(code)) return new String[] {"WES", "ICON"};
        else if("R2300101".equals(code)) return new String[] {"WES", "ASD"};
        else if("S030".equals(code)) return new String[] {"WES", "F8"};
        else if("S032".equals(code)) return new String[] {"WES", "F9"};
        else if("S034".equals(code)) return new String[] {"WES", "VWF"};
        else if("S053".equals(code)) return new String[] {"WES", "F7"};
        else if("S054".equals(code)) return new String[] {"WES", "F11"};
        else if("S055".equals(code)) return new String[] {"WES", "F12"};
        else if("S064".equals(code)) return new String[] {"WES", "PTPN11"};
        else if("S065".equals(code)) return new String[] {"WES", "ATP7B"};
        else if("S067".equals(code)) return new String[] {"WES", "HPRT1"};
        else if("S077".equals(code)) return new String[] {"WES", "SCN5A"};
        else if("S078".equals(code)) return new String[] {"WES", "MYH7"};
        else if("S080".equals(code)) return new String[] {"WES", "SCN1A"};
        else if("S081".equals(code)) return new String[] {"WES", "SPG11"};
        else if("S082".equals(code)) return new String[] {"WES", "DYSF"};
        else if("S083".equals(code)) return new String[] {"WES", "CACNA1A"};
        else if("S084".equals(code)) return new String[] {"WES", "VPS13A"};
        else if("S085".equals(code)) return new String[] {"WES", "LRRK2"};
        else if("S086".equals(code)) return new String[] {"WES", "APP"};
        else if("S087".equals(code)) return new String[] {"WES", "COL2A1"};
        else if("S088".equals(code)) return new String[] {"WES", "PCCA"};
        else if("S089".equals(code)) return new String[] {"WES", "GAA"};
        else if("S090".equals(code)) return new String[] {"WES", "ABCC8"};
        else if("S091".equals(code)) return new String[] {"WES", "CHD7"};
        else if("S092".equals(code)) return new String[] {"WES", "DUOX2"};
        else if("S093".equals(code)) return new String[] {"WES", "FBN1"};
        else if("S102".equals(code)) return new String[] {"WES", "PKD1"};
        else if("S103".equals(code)) return new String[] {"WES", "NIPBL"};
        else if("S104".equals(code)) return new String[] {"WES", "WES"};
        else if("S109".equals(code)) return new String[] {"WES", "prePTPN11"};
        else if("S110".equals(code)) return new String[] {"WES", "preFGFR3"};
        else if("S120".equals(code)) return new String[] {"WES", "COL4A2"};
        else if("S123".equals(code)) return new String[] {"WES", "PHEX"};
        else if("S125".equals(code)) return new String[] {"WES", "OPA1"};
        else if("S126".equals(code)) return new String[] {"WES", "SCN4A"};
        else if("S127".equals(code)) return new String[] {"WES", "SLC12A3"};
        else if("T035".equals(code)) return new String[] {"WES", "WES"};

        else if("H001".equals(code)) return new String[] {"WES", "PSP"};
        else if("H002".equals(code)) return new String[] {"WES", "HTG"};
        else if("H003".equals(code)) return new String[] {"WES", "ID"};
        else if("H004".equals(code)) return new String[] {"WES", "DD"};
        else if("H005".equals(code)) return new String[] {"WES", "KH"};
        else if("H006".equals(code)) return new String[] {"WES", "LD"};
        else if("H007".equals(code)) return new String[] {"WES", "PBD"};
        else if("H008".equals(code)) return new String[] {"WES", "PHD"};
        else if("H009".equals(code)) return new String[] {"WES", "PLD"};
        else if("H010".equals(code)) return new String[] {"WES", "HTP"};
        else if("H011".equals(code)) return new String[] {"WES", "AI"};
        else if("I001".equals(code)) return new String[] {"WES", "HHM"};

        else if("N113".equals(code)) return new String[] {"DGS", "Seq"};
        else if("N127".equals(code)) return new String[] {"DGS", "DGS"};
        else if("N137".equals(code)) return new String[] {"DGS_Trio", null};
        else if("N138".equals(code)) return new String[] {"DGS_Trio", "F"};
        else if("N139".equals(code)) return new String[] {"DGS_Trio", "M"};

        else if("ON001".equals(code)) return new String[] {"Cancer", "OSWCAN"};
        else if("ON040".equals(code)) return new String[] {"Cancer", "OSCAN"};

        else if("N087".equals(code)) return new String[] {"ST", "GSSTK"};
        else if("J020".equals(code)) return new String[] {"ST", "GSSTK"};
        else if("N088".equals(code)) return new String[] {"ST", "GSHPL"};
        else if("J021".equals(code)) return new String[] {"ST", "GSHPL"};
        else if("N089".equals(code)) return new String[] {"ST", "GSH"};
        else if("J019".equals(code)) return new String[] {"ST", "GSH"};
        else if("N112".equals(code)) return new String[] {"ST", "GSH"};
        else if("N185".equals(code)) return new String[] {"ST", "GSH"};
        else if("ON087".equals(code)) return new String[] {"ST", "GSSTK"};
        else if("ON088".equals(code)) return new String[] {"ST", "GSHPL"};
        else if("ON089".equals(code)) return new String[] {"ST", "GSH"};

        else return null;
    }
    private String[] toCode(String code) {
        if(code == null) return null;

        else if("N064".equals(code)) return new String[] {"HEMA", "AML"};
        else if("S051".equals(code)) return new String[] {"HEMA", "AML"};

        // for DGS
        else if("N114".equals(code)) return new String[] {"DGS", "BI"};
        else if("A005".equals(code)) return new String[] {"DGS_Plus", null};
        else if("A006".equals(code)) return new String[] {"DGS_Plus", "F"};
        else if("A007".equals(code)) return new String[] {"DGS_Plus", "M"};
        else if("A008".equals(code)) return new String[] {"DGS_DMS", null};
        else if("A009".equals(code)) return new String[] {"DGS_DMS", "F"};
        else if("A010".equals(code)) return new String[] {"DGS_DMS", "M"};

        return null;
    }
}
