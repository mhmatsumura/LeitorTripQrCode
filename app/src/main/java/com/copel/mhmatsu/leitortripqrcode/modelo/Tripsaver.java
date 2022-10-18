package com.copel.mhmatsu.leitortripqrcode.modelo;

import java.io.Serializable;


public class Tripsaver implements Serializable {

    private Long id;
    private String codigoId;
    private String tcmr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoId() {
        return codigoId;
    }

    public void setCodigoId(String codigoId) {
        this.codigoId = codigoId;
    }

    public String getTcmr() {
        return tcmr;
    }

    public void setTcmr(String tcmr) {
        this.tcmr = tcmr;
    }
}




