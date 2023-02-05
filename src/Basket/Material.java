package Basket;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

class Material {
    private int idMaterial;
    private String nameMaterial;
    private int price;
    private int dateOfDelivery;

    public Material() {
    }

    public Material(int idMaterial, String nameMaterial, int price, int dateOfDelivery) {
        this.idMaterial = idMaterial;
        this.nameMaterial = nameMaterial;
        this.price = price;
        this.dateOfDelivery = dateOfDelivery;
    }

    public Material(String... params) {
        this.idMaterial = Integer.parseInt(params[0]);
        this.nameMaterial = params[1];
        this.price = Integer.parseInt(params[2]);
        this.dateOfDelivery = Integer.parseInt(params[3]);
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNameMaterial() {
        return nameMaterial;
    }

    public void setNameMaterial(String nameMaterial) {
        this.nameMaterial = nameMaterial;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(int dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }


    @Override
    public String toString() {
        return "\n" + nameMaterial + ", Дата Поставки: " + dateOfDelivery;
    }

    public int findPrice(String nameMaterial) {
        return price;
    }

}
