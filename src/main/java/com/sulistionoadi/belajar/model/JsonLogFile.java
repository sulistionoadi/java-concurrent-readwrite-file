/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sulistionoadi.belajar.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adi
 */
public class JsonLogFile {
    
    private Long dataProcessed = 0L;
    private List<String> dataContent = new ArrayList<>();

    public Long getDataProcessed() {
        return dataProcessed;
    }

    public void setDataProcessed(Long dataProcessed) {
        this.dataProcessed = dataProcessed;
    }

    public List<String> getDataContent() {
        return dataContent;
    }

    public void setDataContent(List<String> dataContent) {
        this.dataContent = dataContent;
    }
    
}
