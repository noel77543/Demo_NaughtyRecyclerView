package tw.noel.sung.com.demo_naughtyrecyclerview.test.model;

/**
 * Created by noel on 2018/3/24.
 */

public class People {
    private int height;
    private int weight;
    private int age;
    private String name;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "姓名:"+getName()+"\n年齡:" +getAge()+"\n身高:"+getHeight()+"\n體重:"+getWeight();
    }
}
