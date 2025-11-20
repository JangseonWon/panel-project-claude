package com.gcgenome.lims.client.expand;

import com.gcgenome.lims.api.InterpretationApi;
import com.gcgenome.lims.api.ProgressApi;
import com.gcgenome.lims.api.ReportApi;
import com.gcgenome.lims.api.VersionCheckApi;
import com.gcgenome.lims.client.ExpandElement;
import com.gcgenome.lims.client.WindowState;
import com.gcgenome.lims.client.interpretation.PreviewElement;
import com.gcgenome.lims.client.interpretation.SangerTableElement;
import com.gcgenome.lims.dto.Message;
import com.gcgenome.lims.dto.interpretation.InterpretationParam;
import com.gcgenome.lims.dto.interpretation.Sanger;
import com.gcgenome.lims.test.sanger.TestInfo;
import elemental2.core.Global;
import elemental2.dom.*;
import elemental2.promise.Promise;
import net.sayaya.ui.*;
import net.sayaya.ui.event.HasStateChangeHandlers;
import org.jboss.elemento.HTMLContainerBuilder;

import java.util.*;

import static org.jboss.elemento.Elements.*;

public class SangerExpandElement extends HTMLElementBuilder<HTMLDivElement, SangerExpandElement> implements ExpandElement<HTMLDivElement> {
    public static SangerExpandElement build(String id, long sample, String service) {
        return new SangerExpandElement(div(), id, sample, service);
    }
    private final HTMLContainerBuilder<HTMLDivElement> _this;
    private final VariantsElement iptVariants = new VariantsElement().style("margin-left: 15px; padding-right: 15px; margin-bottom: 16px; margin-bottom: 16px;");
    private final TextAreaElement<String> iptInterpretation = TextAreaElement.textBox().outlined().style("width: -webkit-fill-available; margin-top: 20px;height: 130px;").text("Interpretation");
    private final DropDownElement iptInspector = DropDownElement.outlined(ListElement.singleLineList()
                    .add(ListElement.singleLine().label(""))
                    .add(ListElement.singleLine().label("김경미"))
                    .add(ListElement.singleLine().label("류해인"))
                    .add(ListElement.singleLine().label("문예솔"))
                    .add(ListElement.singleLine().label("김다솜")))
            .css("input").text("검사자").style("width: 100px;")
            .select("김경미");
    private final DropDownElement iptReporter = DropDownElement.outlined(ListElement.singleLineList()
                    .add(ListElement.singleLine().label(""))
                    .add(ListElement.singleLine().label("설창안"))
                    .add(ListElement.singleLine().label("기창석"))
                    .add(ListElement.singleLine().label("이새미"))
                    .add(ListElement.singleLine().label("이청화"))
                    .add(ListElement.singleLine().label("조은해")))
            .css("input").text("보고자").style("width: 100px;")
            .select("설창안");
    private final DropDownElement iptReviewer = DropDownElement.outlined(ListElement.singleLineList()
                    .add(ListElement.singleLine().label(""))
                    .add(ListElement.singleLine().label("이새미"))
                    .add(ListElement.singleLine().label("기창석"))
                    .add(ListElement.singleLine().label("설창안"))
                    .add(ListElement.singleLine().label("이청화"))
                    .add(ListElement.singleLine().label("조은해")))
            .css("input").text("검토자").style("width: 100px;")
            .select("이새미");
    private DropDownElement iptProband = DropDownElement.outlined(ListElement.singleLineList()
                    .add(ListElement.singleLine().label("유"))
                    .add(ListElement.singleLine().label("무")))
            .css("input").text("Proband 검체 유/무").style("width: 200px;")
            .select("유");
    private final HTMLContainerBuilder<HTMLDivElement> controller = div().css("controller");
    private final ButtonElement btnAppend = ButtonElement.outline().css("button").before(IconElement.icon("add_circle")).text("Append Variant");
    private final ButtonElement btnRemove = ButtonElement.outline().css("button").before(IconElement.icon("remove_circle")).text("Remove Variant");
    private final ButtonElement btnAuto = ButtonElement.outline().css("button").before(IconElement.icon("smart_button")).text("Interpretation");
    private final ButtonElement btnSave = ButtonElement.outline().css("button").before(IconElement.icon("save")).text("Save");
    private final ButtonElement btnPreview = ButtonElement.outline().css("button").before(IconElement.icon("preview")).text("Preview");
    private final ButtonElement btnHide = ButtonElement.outline().css("button").before(IconElement.icon("close")).text("Close");
    private final String id;
    private final long sample;
    private final String service;
    private final TestInfo test;
    private SangerExpandElement(HTMLContainerBuilder<HTMLDivElement> e, String id, long sample, String service) {
        super(e);
        _this = e.css("work").style("height: 100vh;");
        this.id = id;
        this.sample = sample;
        this.service = service;
        test = Arrays.stream(TestInfo.TESTS).filter(t->t.code().equals(service)).findAny().orElse(null);
        layout(test);
        btnHide.onClick(evt->fireStateChangeEvent());
        btnSave.onClick(evt->save());
        btnAppend.onClick(evt->iptVariants.append());
        btnRemove.onClick(evt->iptVariants.remove());
        btnAuto.onClick(evt->auto());
        btnPreview.onClick(evt->preview());
    }

    private void layout(TestInfo test) {
        _this.add(div().style("height: calc(100% - 55px); overflow: auto;")
                .add(iptVariants)
                .add(div().style("margin-left: 15px; padding-right: 15px; margin-bottom: 16px;").add(iptInterpretation))
                .add(div().style("display: flex; align-items: center; margin-left: 15px; margin-right: 15px; gap: 10px;")
                        .add(test != null && test.isFamilialMutationTest() ? iptProband : null)
                        .add(div().style("flex-grow: 1"))
                        .add(div().style("text-align: right;").add(iptInspector).add(iptReporter).add(iptReviewer)))
        );

        _this.add(controller.add(span().add(btnAppend).add(btnRemove).add(btnAuto))
                .add(span().style("margin-left: 10px;").add(btnSave).add(btnPreview).add(btnHide)));
    }

    @Override
    public void update() {
        Message msg = Message.builder().id(id).type(Message.MessageType.STRETCH).build();
        DomGlobal.window.parent.postMessage(Global.JSON.stringify(msg), "*");
        InterpretationApi.interpretation(sample, service).then(reports->{
            this.update((Sanger)reports);
            return null;
        });
        iptVariants.append().append();
        ReportApi.state(sample, service).then(state->{
            boolean isFinal = "F".equalsIgnoreCase(state);
            btnSave.enabled(!isFinal);
            btnPreview.enabled(!isFinal);
            btnSave.text(isFinal?"검사 완료":"SAVE");
            return null;
        });
    }
    private Sanger value() {
        return new Sanger()
                .variants(iptVariants.values())
                .interpretation(iptInterpretation.value())
                .inspector(iptInspector.value())
                .reporter(iptReporter.value())
                .reviewer(iptReviewer.value())
                .hasProbandSpecimen("유".equals(iptProband.value()));
    }
    private void update(Sanger dto) {
        if(dto == null) {
            dto = new Sanger()
                    .variants(new Sanger.Variant[]{new Sanger.Variant().clazz("-")})
                    .hasProbandSpecimen(true);
        }
        iptVariants.values(dto.variants());
        iptInterpretation.value(dto.interpretation());
        iptInspector.select(dto.inspector());
        iptReporter.select(dto.reporter());
        iptReviewer.select(dto.reviewer());
        iptProband.select(dto.hasProbandSpecimen() == null || dto.hasProbandSpecimen() ? "유" : "무");
    }
    public void save() {
        if(!DomGlobal.confirm("저장합니다.")) return;
        InterpretationApi.save(sample, service, value())
                .then(callback->{
                    update();
                    DomGlobal.alert("저장되었습니다.");
                    return null;
                });
    }
    public void auto() {
        if(!DomGlobal.confirm("입력된 정보를 바탕으로 판독 문구를 자동 생성합니다.")) return;
        InterpretationApi.auto(sample, service, new InterpretationParam().previous(value()))
                .then(r->{
                    update((Sanger) r);
                    DomGlobal.alert("생성되었습니다. 저장하세요.");
                    return null;
                });
    }
    public void negative() {
        if(!DomGlobal.confirm("음성 결과를 입력합니다.")) return;
        InterpretationApi.negative(sample, service)
                .then(obj->{
                    Sanger result = (Sanger)obj;
                    update(result);
                    DomGlobal.alert("생성되었습니다. 저장하세요.");
                    return null;
                });
    }
    public void preview() {
        Sanger result = value();
        ReportApi.preview(sample, service, result).then(blob->{
            PreviewElement preview = PreviewElement.build(blob);
            preview.onConfirm(confirm->{
                if(!DomGlobal.confirm("이대로 결과지를 생성하고 완료 합니다.")) return;
                ProgressApi.open(true);
                ProgressApi.progress(0.3);
                VersionCheckApi.isNew(sample, service)
                        .then(isNew->isNew? Promise.resolve(""):DescriptionDialog.dialog())
                        .then(description->InterpretationApi.save(sample, service, result).finally_(()->ProgressApi.progress(0.6))
                                .then(saved->ReportApi.print(sample, service, description))).then(report->{
                            ProgressApi.progress(0.9);
                            return ReportApi.publish(sample, service, report.createAt());
                        }).then(e->{
                            DomGlobal.alert("검사가 완료되었습니다.");
                            return null;
                        }).finally_(()->{
                            update();
                            ProgressApi.close();
                            preview.element().remove();
                        });
            });
            _this.add(preview);
            return null;
        });
    }
    @Override
    public SangerExpandElement that() {
        return this;
    }

    private final Set<HasStateChangeHandlers.StateChangeEventListener<WindowState>> listeners = new HashSet<>();
    @Override
    public Collection<HasStateChangeHandlers.StateChangeEventListener<WindowState>> listeners() {
        return listeners;
    }

    @Override
    public WindowState state() {
        return WindowState.COLLAPSE;
    }
    private static final class VariantsElement extends HTMLElementBuilder<HTMLDivElement, VariantsElement> {
        private final HTMLContainerBuilder<HTMLDivElement> _this;
        private final List<VariantElement> variants = new LinkedList<>();
        public VariantsElement() {
            this(div());
        }
        public VariantsElement(HTMLContainerBuilder<HTMLDivElement> e) {
            super(e);
            _this = e;
        }

        public VariantsElement append() {
            VariantElement child = new VariantElement();
            variants.add(child);
            _this.add(child);
            return that();
        }

        public VariantsElement remove() {
            VariantElement child = variants.get(variants.size()-1);
            variants.remove(child);
            child.element().remove();
            return that();
        }

        public VariantsElement values(Sanger.Variant[] values) {
            variants.clear();
            _this.element().innerHTML = "";
            if(values != null) for (Sanger.Variant value : values) {
                VariantElement child = new VariantElement().value(value);
                variants.add(child);
                _this.add(child);
            }
            return that();
        }
        public Sanger.Variant[] values() {
            return variants.stream().map(v->v.value()).toArray(Sanger.Variant[]::new);
        }

        @Override
        public VariantsElement that() {
            return this;
        }
    }

    private static final class VariantElement extends HTMLElementBuilder<HTMLDivElement, VariantElement> {
        private SangerTableElement table = SangerTableElement.build();
        private HTMLDivElement canvas = div().attr("tabindex", "0").attr("contenteditable", "true")
                .style("width: 100%; height: 180px; border: 1px solid #888;" +
                        "display: flex;align-items: center;justify-content: center;").element();
        public VariantElement() {
            this(div().style("margin-top: 20px;"));
        }
        private VariantElement(HTMLContainerBuilder<HTMLDivElement> e) {
            super(e);
            e.add(table.update(new Sanger.Variant().clazz("-")))
                    .add(canvas);
            canvas.addEventListener("keydown", evt->{
                KeyboardEvent cast = (KeyboardEvent)evt;
                if(cast.key!=null && (cast.key.length() == 1) &&!cast.ctrlKey) {
                    evt.preventDefault();
                } else if(cast.key!=null) {
                    switch(cast.key) {
                        case "Esc":
                        case "Escape" :
                        case "Left":
                        case "ArrowLeft":
                        case "Right":
                        case "ArrowRight":
                        case "Up":
                        case "ArrowUp":
                        case "Enter":
                        case "Down":
                        case "ArrowDown": canvas.blur();
                    }
                    evt.stopPropagation();
                    evt.stopImmediatePropagation();
                }
            });
            canvas.addEventListener("keypress", Event::preventDefault);
            canvas.addEventListener("input", Event::preventDefault);
            canvas.addEventListener("paste", evt->canvas.innerHTML = "");
        }
        public VariantElement value(Sanger.Variant value) {
            table.update(value);
            HTMLImageElement img = img().element();
            img.src = value.sanger();
            canvas.appendChild(img);
            return that();
        }
        public Sanger.Variant value() {
            HTMLImageElement e = (HTMLImageElement) canvas.firstElementChild;
            return table.value().sanger(e.src);
        }
        @Override
        public VariantElement that() {
            return this;
        }
    }
}
