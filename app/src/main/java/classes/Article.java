package classes;

import java.time.LocalDate;

public class Article {

    private String id ;
    private String name ;
    private String desc ;
    private String img ;
    private int quantity ;
    private Double price    ;
    private String expirationDate ;

    public Article(){
    }

    public Article(String id, String name, String desc, String img, int quantity, Double price, String  expirationDate) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.quantity = quantity;
        this.price = price;
        this.expirationDate = expirationDate;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
