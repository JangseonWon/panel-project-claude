package com.gcgenome.lims.client;

import elemental2.promise.Promise;
import net.sayaya.ui.*;

import static org.jboss.elemento.Elements.body;

public class DescriptionDialog {
    public static Promise<String> dialog() {
        ButtonElementText print = ButtonElement.outline().text("PRINT").style("margin-left:0;").css("button").before(IconElement.icon("preview"));
        ButtonElementText cancel = ButtonElement.outline().text("CANCEL").css("button").before(IconElement.icon("close"));
        TextAreaElement<String> iptDescription = TextAreaElement.textBox().outlined().text("Description").autocomplete("").style("width: 100%; height: 15em; margin-top: 10px;");
        iptDescription.element().firstElementChild.setAttribute("style", "width: 20em;");
        Dialog dialog = Dialog.alert("수정사항(전후 비교기재, 상세작성)", cancel, print);
        dialog.add(iptDescription);
        body().add(dialog);
        dialog.open();
        return new Promise<>((resolve, reject) -> {
            print.onClick(evt ->{
                dialog.close();
                dialog.element().remove();
                resolve.onInvoke(iptDescription.value());
            });

            cancel.onClick(evt -> {
                dialog.close();
                dialog.element().remove();
                reject.onInvoke(null);
            });
        });
    }
}
