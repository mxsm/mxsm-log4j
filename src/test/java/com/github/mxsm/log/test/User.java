package com.github.mxsm.log.test;

/**
 * @author mxsm
 * @date 2021/12/18 17:33
 * @Since 1.0.0
 */
public class User {

    private String name;

    private String address;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", age=" + age +
            '}';
    }
}
