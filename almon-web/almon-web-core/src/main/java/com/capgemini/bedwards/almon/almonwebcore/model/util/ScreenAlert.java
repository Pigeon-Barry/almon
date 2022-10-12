package com.capgemini.bedwards.almon.almonwebcore.model.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScreenAlert {
    private String message;
    private Type type;


    public String getHtmlClass() {
        return this.type.CLASS;
    }
    public enum Type {
        SUCCESS("alert-success"),
        WARNING("alert-warning"),
        ERROR("alert-danger"),


        PRIMARY("alert-primary"),
        SECONDARY("alert-secondary"),
        INFO("alert-info"),
        LIGHT("alert-light"),
        DARK("alert-dark"),
        ;

        private String CLASS;

        Type(String _class) {
            this.CLASS = _class;
        }
    }
}
