package com.roodie.pifagormatrix.model;

/**
 * Created by Roodie on 15.03.2015.
 */
public class MatrixItem {

    private String description;
    private int id;
    private String matrixDetails;
    private String[] matrixGridData;
    private String table;
    private String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatrixDetails() {
        return matrixDetails;
    }

    public void setMatrixDetails(String matrixDetails) {
        this.matrixDetails = matrixDetails;
    }

    public String[] getMatrixGridData() {
        return matrixGridData;
    }

    public void setMatrixGridData(String[] matrixGridData) {
        this.matrixGridData = matrixGridData;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


