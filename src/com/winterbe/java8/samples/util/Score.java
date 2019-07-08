package com.winterbe.java8.samples.util;

import java.util.function.BiFunction;

/**
 * @author onlyone
 */
public class Score {

    private String name;
    private Long   yuwen;
    private Long   shuxue;

    public Score(){
    }

    public Score(String name){
        this.name = name;
    }

    public Score(String name, Long yuwen) {
        this.name = name;
        this.yuwen = yuwen;
    }

    public Score(String name, Long yuwen, Long shuxue){
        this.name = name;
        this.yuwen = yuwen;
        this.shuxue = shuxue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getYuwen() {
        return yuwen;
    }

    public Long getShuxue() {
        return shuxue;
    }

    public void setYuwen(Long yuwen) {
        this.yuwen = yuwen;
    }

    public void setShuxue(Long shuxue) {
        this.shuxue = shuxue;
    }

    public void printYuwenScore(BiFunction<String, Long, String> scoreFunction) {
        System.out.println(scoreFunction.apply(name, yuwen));
    }

    @Override
    public String toString() {
        return "Score{" + "name='" + name + '\'' + ", yuwen=" + yuwen + ", shuxue=" + shuxue + '}';
    }
}
