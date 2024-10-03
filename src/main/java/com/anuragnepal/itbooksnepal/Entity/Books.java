package com.anuragnepal.itbooksnepal.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Integer price;
    @Column
    private Double rating;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] pdfData;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] imgData;
    @Column
    private String category;

}
