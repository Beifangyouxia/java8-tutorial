package com.winterbe.java8.samples.methodreference.util;

/**
 * @author onlyone
 */
public class Score {

    private String name;
    private Long   yuwen;
    private Long   shuxue;

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

    @Override
    public String toString() {
        return "Score{" + "name='" + name + '\'' + ", yuwen=" + yuwen + ", shuxue=" + shuxue + '}';
    }
}
