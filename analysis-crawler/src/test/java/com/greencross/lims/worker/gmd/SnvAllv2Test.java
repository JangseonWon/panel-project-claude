package com.greencross.lims.worker.gmd;

import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.Request;
import com.greencross.lims.worker.Tester;
import com.greencross.lims.worker.TesterImpl;
import com.greencross.lims.worker.gmd.impl.v2.SnvAllv2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@Disabled
@ExtendWith(SpringExtension.class)
class SnvAllv2Test {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final static Tester N037 = new TesterImpl("21GMD043_20210510-171-5259_ARH-19_M.annotation.txt", 202105101715259L,
													  new Request().pk(new Request.RequestPK().sample(202105101715259L).service("N037")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 19, "202105101715259:N037"))
															  .panel("ARH").serial("21GMD043_20210510-171-5259_ARH-19_M")
															  .sample(202105101715259L).service("N037"));
	private final static Tester N038 = new TesterImpl("21GMD042_20210507-171-5156_CM-24_M.annotation.txt", 202105071715156L,
													  new Request().pk(new Request.RequestPK().sample(202105071715156L).service("N038")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 24, "202105071715156:N038"))
															  .panel("CM").serial("21GMD042_20210507-171-5156_CM-24_M")
															  .sample(202105071715156L).service("N038"));
	private final static Tester N039 = new TesterImpl("21GMD036_20210422-171-5055_PID-12_M.annotation.txt", 202104221715055L,
													  new Request().pk(new Request.RequestPK().sample(202104221715055L).service("N039")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD036", 12, "202104221715055:N039"))
															  .panel("PID").serial("21GMD036_20210422-171-5055_PID-12_M")
															  .sample(202104221715055L).service("N039"));
	private final static Tester N040 = new TesterImpl("21GMD040_20210428-171-5004_ATX-5_M.annotation.txt", 202104281715004L,
													  new Request().pk(new Request.RequestPK().sample(202104281715004L).service("N040")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 5, "202104281715004:N040"))
															  .panel("ATX").serial("21GMD040_20210428-171-5004_ATX-5_M")
															  .sample(202104281715004L).service("N040"));
	private final static Tester N041 = new TesterImpl("21GMD043_20210508-171-5144_SD-9_M.annotation.txt", 202105081715144L,
													  new Request().pk(new Request.RequestPK().sample(202105081715144L).service("N041")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 9, "202105081715144:N041"))
															  .panel("SD").serial("21GMD043_20210508-171-5144_SD-9_M")
															  .sample(202105081715144L).service("N041"));
	private final static Tester N042 = new TesterImpl("21GMD042_20210503-171-5057_SS-2_M.annotation.txt", 202105031715057L,
													  new Request().pk(new Request.RequestPK().sample(202105031715057L).service("N042")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 2, "202105031715057:N042"))
															  .panel("SS").serial("21GMD042_20210503-171-5057_SS-2_M")
															  .sample(202105031715057L).service("N042"));
	private final static Tester N043 = new TesterImpl("21GMD040_20210428-171-5004_ATX-5_M.annotation.txt", 202104281715004L,
													  new Request().pk(new Request.RequestPK().sample(202104281715004L).service("N043")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 5, "202104281715004:N043"))
															  .panel("ATX").serial("21GMD040_20210428-171-5004_ATX-5_M")
															  .sample(202104281715004L).service("N043"));
	private final static Tester N044 = new TesterImpl("21GMD042_20210507-171-5188_MD-27_M.annotation.txt", 202105071715188L,
													  new Request().pk(new Request.RequestPK().sample(202105071715188L).service("N044")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 27, "202105071715188:N044"))
															  .panel("MD").serial("21GMD042_20210507-171-5188_MD-27_M")
															  .sample(202105071715188L).service("N044"));
	private final static Tester N045 = new TesterImpl("21GMD042_20210503-171-5249_MP-3_M.annotation.txt", 202105031715249L,
													  new Request().pk(new Request.RequestPK().sample(202105031715249L).service("N045")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 3, "202105031715249:N045"))
															  .panel("MP").serial("21GMD042_20210503-171-5249_MP-3_M")
															  .sample(202105031715249L).service("N045"));
	private final static Tester N046 = new TesterImpl("21GMD042_20210507-171-5225_ATX-31_M.annotation.txt", 202105071715225L,
													  new Request().pk(new Request.RequestPK().sample(202105071715225L).service("N046")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 31, "202105071715225:N046"))
															  .panel("ATX").serial("21GMD042_20210507-171-5225_ATX-31_M")
															  .sample(202105071715225L).service("N046"));
	private final static Tester N047 = new TesterImpl("21GMD042_20210506-171-5186_SPG-18_M.annotation.txt", 202105061715186L,
													  new Request().pk(new Request.RequestPK().sample(202105061715186L).service("N047")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 18, "202105061715186:N047"))
															  .panel("SPG").serial("21GMD042_20210506-171-5186_SPG-18_M")
															  .sample(202105061715186L).service("N047"));
	private final static Tester N048 = new TesterImpl("21GMD038_20210423-171-5036_DSD-8_M.annotation.txt", 202104231715036L,
													  new Request().pk(new Request.RequestPK().sample(202104231715036L).service("N048")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD038", 8, "202104231715036:N048"))
															  .panel("DSD").serial("21GMD038_20210423-171-5036_DSD-8_M")
															  .sample(202104231715036L).service("N048"));
	private final static Tester N049 = new TesterImpl("21GMD025_20210317-171-5104_CM-3_M.annotation.txt", 202103171715104L,
													  new Request().pk(new Request.RequestPK().sample(202103171715104L).service("N049")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD025", 3, "202103171715104:N049"))
															  .panel("CM").serial("21GMD025_20210317-171-5104_CM-3_M")
															  .sample(202103171715104L).service("N049"));
	private final static Tester N050 = new TesterImpl("21GMD043_20210511-171-5156_IEM-24_M.annotation.txt", 202105111715156L,
													  new Request().pk(new Request.RequestPK().sample(202105111715156L).service("N050")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 24, "202105111715156:N050"))
															  .panel("IEM").serial("21GMD043_20210511-171-5156_IEM-24_M")
															  .sample(202105111715156L).service("N050"));
	private final static Tester N051 = new TesterImpl("21GMD040_20210428-171-5004_ATX-5_M.annotation.txt", 202104281715004L,
													  new Request().pk(new Request.RequestPK().sample(202104281715004L).service("N051")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 5, "202104281715004:N051"))
															  .panel("ATX").serial("21GMD040_20210428-171-5004_ATX-5_M")
															  .sample(202104281715004L).service("N051"));
	private final static Tester N052 = new TesterImpl("21GMD040_20210428-171-5146_CH-12_M.annotation.txt", 202104281715146L,
													  new Request().pk(new Request.RequestPK().sample(202104281715146L).service("N052")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 12, "202104281715146:N052"))
															  .panel("CH").serial("21GMD040_20210428-171-5146_CH-12_M")
															  .sample(202104281715146L).service("N052"));
	private final static Tester N053 = new TesterImpl("21GMD042_20210504-171-5182_COAG-9_M.annotation.txt", 202105041715182L,
													  new Request().pk(new Request.RequestPK().sample(202105041715182L).service("N053")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 9, "202105041715182:N053"))
															  .panel("COAG").serial("21GMD042_20210504-171-5182_COAG-9_M")
															  .sample(202105041715182L).service("N053"));
	private final static Tester N054 = new TesterImpl("21GMD043_20210510-171-5079_EPIL-11_M.annotation.txt", 202105101715079L,
													  new Request().pk(new Request.RequestPK().sample(202105101715079L).service("N054")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 11, "202105101715079:N054"))
															  .panel("EPIL").serial("21GMD043_20210510-171-5079_EPIL-11_M")
															  .sample(202105101715079L).service("N054"));
	private final static Tester N055 = new TesterImpl("21GMD043_20210510-171-5075_CMT-10_M.annotation.txt", 202105101715075L,
													  new Request().pk(new Request.RequestPK().sample(202105101715075L).service("N055")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 10, "202105101715075:N055"))
															  .panel("CMT").serial("21GMD043_20210510-171-5075_CMT-10_M")
															  .sample(202105101715075L).service("N055"));
	private final static Tester N062 = new TesterImpl("21GMD043_20210402-171-5037_PID-1_M.annotation.txt", 202104021715037L,
													  new Request().pk(new Request.RequestPK().sample(202104021715037L).service("N062")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 1, "202104021715037:N062"))
															  .panel("PID").serial("21GMD043_20210402-171-5037_PID-1_M")
															  .sample(202104021715037L).service("N062"));
	private final static Tester N067 = new TesterImpl("21GMD039_20210426-171-5256_IGD-6_M.annotation.txt", 202104261715256L,
													  new Request().pk(new Request.RequestPK().sample(202104261715256L).service("N067")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD039", 6, "202104261715256:N067"))
															  .panel("IGD").serial("21GMD039_20210426-171-5256_IGD-6_M")
															  .sample(202104261715256L).service("N067"));
	private final static Tester N071 = new TesterImpl("21GMD043_20210507-171-5045_RP-3_M.annotation.txt", 202105071715045L,
													  new Request().pk(new Request.RequestPK().sample(202105071715045L).service("N071")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 3, "202105071715045:N071"))
															  .panel("RP").serial("21GMD043_20210507-171-5045_RP-3_M")
															  .sample(202105071715045L).service("N071"));
	private final static Tester N072 = new TesterImpl("21GMD043_20210507-171-5158_EYE-5_M.annotation.txt", 202105071715158L,
													  new Request().pk(new Request.RequestPK().sample(202105071715158L).service("N072")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 5, "202105071715158:N072"))
															  .panel("EYE").serial("21GMD043_20210507-171-5158_EYE-5_M")
															  .sample(202105071715158L).service("N072"));
	private final static Tester N073 = new TesterImpl("21GMD040_20210429-171-5154_HL-27_M.annotation.txt", 202104291715154L,
													  new Request().pk(new Request.RequestPK().sample(202104291715154L).service("N073")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 27, "202104291715154:N073"))
															  .panel("HL").serial("21GMD040_20210429-171-5154_HL-27_M")
															  .sample(202104291715154L).service("N073"));
	private final static Tester N078 = new TesterImpl("21GMD043_20210507-171-5193_PKS-6_M.annotation.txt", 202105071715193L,
													  new Request().pk(new Request.RequestPK().sample(202105071715193L).service("N078")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 6, "202105071715193:N078"))
															  .panel("PKS").serial("21GMD043_20210507-171-5193_PKS-6_M")
															  .sample(202105071715193L).service("N078"));
	private final static Tester N080 = new TesterImpl("21GMD041_20210430-171-5060_DEM-14_M.annotation.txt", 202104301715060L,
													  new Request().pk(new Request.RequestPK().sample(202104301715060L).service("N080")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD041", 14, "202104301715060:N080"))
															  .panel("DEM").serial("21GMD041_20210430-171-5060_DEM-14_M")
															  .sample(202104301715060L).service("N080"));
	private final static Tester N081 = new TesterImpl("21GMD043_20210510-171-5146_MODY-16_M.annotation.txt", 202105101715146L,
													  new Request().pk(new Request.RequestPK().sample(202105101715146L).service("N081")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 16, "202105101715146:N081"))
															  .panel("MODY").serial("21GMD043_20210510-171-5146_MODY-16_M")
															  .sample(202105101715146L).service("N081"));
	private final static Tester N084 = new TesterImpl("21GMD043_20210510-171-5237_DYT-18_M.annotation.txt", 202105101715237L,
													  new Request().pk(new Request.RequestPK().sample(202105101715237L).service("N084")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 18, "202105101715237:N084"))
															  .panel("DYT").serial("21GMD043_20210510-171-5237_DYT-18_M")
															  .sample(202105101715237L).service("N084"));
	private final static Tester N095 = new TesterImpl("21GMD043_20210510-171-5126_AHUS-14_M.annotation.txt", 202105101715126L,
													  new Request().pk(new Request.RequestPK().sample(202105101715126L).service("N095")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 14, "202105101715126:N095"))
															  .panel("AHUS").serial("21GMD043_20210510-171-5126_AHUS-14_M")
															  .sample(202105101715126L).service("N095"));
	private final static Tester N096 = new TesterImpl("21GMD043_20210510-171-5128_ALP-15_M.annotation.txt", 202105101715128L,
													  new Request().pk(new Request.RequestPK().sample(202105101715128L).service("N096")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 15, "202105101715128:N096"))
															  .panel("ALP").serial("21GMD043_20210510-171-5128_ALP-15_M")
															  .sample(202105101715128L).service("N096"));
	private final static Tester N097 = new TesterImpl("21GMD017_20210302-171-5000_AUT-4_M.annotation.txt", 202103021715000L,
													  new Request().pk(new Request.RequestPK().sample(202103021715000L).service("N097")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD017", 4, "202103021715000:N097"))
															  .panel("AUT").serial("21GMD017_20210302-171-5000_AUT-4_M")
															  .sample(202103021715000L).service("N097"));
	private final static Tester N098 = new TesterImpl("21GMD040_20210429-171-5033_PKD-22_M.annotation.txt", 202104291715033L,
													  new Request().pk(new Request.RequestPK().sample(202104291715033L).service("N098")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 22, "202104291715033:N098"))
															  .panel("PKD").serial("21GMD040_20210429-171-5033_PKD-22_M")
															  .sample(202104291715033L).service("N098"));
	private final static Tester N099 = new TesterImpl("21GMD042_20210504-171-5090_HPL-4_M.annotation.txt", 202105041715090L,
													  new Request().pk(new Request.RequestPK().sample(202105041715090L).service("N099")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD042", 4, "202105041715090:N099"))
															  .panel("HPL").serial("21GMD042_20210504-171-5090_HPL-4_M")
															  .sample(202105041715090L).service("N099"));
	private final static Tester N100 = new TesterImpl("21GMD039_20210427-171-5036_STK-13_M.annotation.txt", 202104271715036L,
													  new Request().pk(new Request.RequestPK().sample(202104271715036L).service("N100")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD039", 13, "202104271715036:N100"))
															  .panel("STK").serial("21GMD039_20210427-171-5036_STK-13_M")
															  .sample(202104271715036L).service("N100"));
	private final static Tester N106 = new TesterImpl("21GMD021_20210315-171-5032_AUT-11_M.annotation.txt", 202103151715032L,
													  new Request().pk(new Request.RequestPK().sample(202103151715032L).service("N106")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD021", 11, "202103151715032:N106"))
															  .panel("AUT").serial("21GMD021_20210315-171-5032_AUT-11_M")
															  .sample(202103151715032L).service("N106"));
	private final static Tester N107 = new TesterImpl("21GMD043_20210511-171-5191_AUT-26_M.annotation.txt", 202105111715191L,
													  new Request().pk(new Request.RequestPK().sample(202105111715191L).service("N107")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 26, "202105111715191:N107"))
															  .panel("AUT").serial("21GMD043_20210511-171-5191_AUT-26_M")
															  .sample(202105111715191L).service("N107"));
	private final static Tester N108 = new TesterImpl("21GMD020_20210312-171-5167_PCD-15_M.annotation.txt", 202103121715167L,
													  new Request().pk(new Request.RequestPK().sample(202103121715167L).service("N108")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD020", 15, "202103121715167:N108"))
															  .panel("PCD").serial("21GMD020_20210312-171-5167_PCD-15_M")
															  .sample(202103121715167L).service("N108"));
	private final static Tester N110 = new TesterImpl("21GMD043_20210511-171-5190_ND-25_M.annotation.txt", 202105111715190L,
													  new Request().pk(new Request.RequestPK().sample(202105111715190L).service("N110")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 25, "202105111715190:N110"))
															  .panel("ND").serial("21GMD043_20210511-171-5190_ND-25_M")
															  .sample(202105111715190L).service("N110"));
	private final static Tester N119 = new TesterImpl("21GMD040_20210428-171-5004_ATX-5_M.annotation.txt", 202104281715004L,
													  new Request().pk(new Request.RequestPK().sample(202104281715004L).service("N119")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 5, "202104281715004:N119"))
															  .panel("ATX").serial("21GMD040_20210428-171-5004_ATX-5_M")
															  .sample(202104281715004L).service("N119"));
	private final static Tester N122 = new TesterImpl("21GMD036_20210419-171-5041_mtDNA-10_M.annotation.txt", 202104191715041L,
													  new Request().pk(new Request.RequestPK().sample(202104191715041L).service("N122")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD036", 10, "202104191715041:N122"))
															  .panel("mtDNA").serial("21GMD036_20210419-171-5041_mtDNA-10_M")
															  .sample(202104191715041L).service("N122"));
	private final static Tester N123 = new TesterImpl("21GMD040_20210429-171-5090_CHOL-24_M.annotation.txt", 202104291715090L,
													  new Request().pk(new Request.RequestPK().sample(202104291715090L).service("N123")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD040", 24, "202104291715090:N123"))
															  .panel("CHOL").serial("21GMD040_20210429-171-5090_CHOL-24_M")
															  .sample(202104291715090L).service("N123"));
	private final static Tester N130 = new TesterImpl("21GMD043_20210510-171-5263_ALS-20_M.annotation.txt", 202105101715263L,
													  new Request().pk(new Request.RequestPK().sample(202105101715263L).service("N130")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD043", 20, "202105101715263:N130"))
															  .panel("ALS").serial("21GMD043_20210510-171-5263_ALS-20_M")
															  .sample(202105101715263L).service("N130"));
	private final static Tester N148 = new TesterImpl("21GMD038_20210423-171-5227_ANE-20_M.annotation.txt", 202104231715227L,
													  new Request().pk(new Request.RequestPK().sample(202104231715227L).service("N148")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("01db4ff4-9689-47b0-b0d9-1659788912c5"), "21GMD038", 20, "202104231715227:N148"))
															  .panel("ANE").serial("21GMD038_20210423-171-5227_ANE-20_M")
															  .sample(202104231715227L).service("N148"));
	private final static Tester N058 = new TesterImpl("21GMD031_20210405-171-5034_DES-21_M.annotation.txt", 202104051715034L,
													  new Request().pk(new Request.RequestPK().sample(202104051715034L).service("N058")),
													  new Analysis(new Analysis.AnalysisPK(UUID.fromString("497ff234-fe85-4328-9d1e-55debae35193"), "21GMD031", 21, "202104051715034:N058"))
															  .panel("DES").serial("21GMD031_20210405-171-5034_DES-21_M")
															  .sample(202104051715034L).service("N058"));
	private final static Tester[] TESTERS = new Tester[] {
		N037, N038, N039,
		N040, N041, N042, N043, N044, N045, N046, N047, N048, N049,
		N050, N051, N052, N053, N054, N055,
		N062, N067,
		N071, N072, N073, N078,
		N080, N081, N084,
		N095, N096, N097, N098, N099,
		N100, N106, N107, N108,
		N110, N119,
		N122, N123,
		N130, N148,
		N058
	};

	@Test
	@DisplayName("파일명으로부터 Sample ID를 제대로 추출하는지 테스트")
	void testSample() {
		for(Tester test: TESTERS) {
			Log.info(test.input());
			test.testSampleExtract(SnvAllv2.PROCESSOR.sample(test.input()));
		}
	}
	@Test
	@DisplayName("파일명으로부터 Analysis 객체를 제대로 생성하는지 테스트")
	void testAnalysis() {
		for(Tester test: TESTERS) {
			Log.info(test.input());
			test.testAnalysisExtract(SnvAllv2.PROCESSOR.map(test.input(), test.requestSample()));
		}
	}
}
