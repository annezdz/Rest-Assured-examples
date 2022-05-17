package br.udemy.anne.rest;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

//@XmlRootElement(name = "user")
//@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    private String name;
    private Integer age;
    private Double salary;
//    @XmlAttribute
    private Long id;

    public User() {
    }

    public User(String name, Integer age, Double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                ", id=" + id +
                '}';
    }
}
