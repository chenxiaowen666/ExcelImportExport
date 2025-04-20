package com.example.demo.service.impl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResult {
    private boolean success;
    private List<String> errors;

    public ImportResult() {
        this.success = true;
        this.errors = new ArrayList<>();
    }

    public ImportResult(boolean success, List<String> errors) {
        this.success = success;
        this.errors = errors;
    }
}