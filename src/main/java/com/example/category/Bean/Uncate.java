package com.example.category.Bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
public class Uncate implements Serializable {
    private int id;
    private String unnum;
    private String unname;
    private int unlevel;
    private int deleted;
    private String parent;
    private List<Uncate> children = new ArrayList<>();
}
