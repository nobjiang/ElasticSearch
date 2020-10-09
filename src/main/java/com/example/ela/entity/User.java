package com.example.ela.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: ela
 * @description:
 * @author: zhaol
 * @create: 2020-10-08 13:52
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private int age;
}