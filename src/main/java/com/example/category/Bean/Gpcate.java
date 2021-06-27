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
public class Gpcate implements Serializable {
    private int id;
    private String gpcnum;
    private String gpcname;
    private int gpclevel;
    private int deleted;
    private String parent;
    private List<Gpcate> children = new ArrayList<>();
}
