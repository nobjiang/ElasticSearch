package com.example.ela.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ela
 * @description:
 * @author: zhaol
 * @create: 2020-10-08 16:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    private String title;
    private String img;
    private String price;
}