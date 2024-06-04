package classes;

import java.time.LocalDate;
import java.util.Calendar;

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



    // calculate  expired products


    public boolean isExpired() {
        // Parse expiration date string into day, month, and year components
        String[] parts = this.expirationDate.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; // Month is zero-based in Calendar
        int year = Integer.parseInt(parts[2]);

        // Create Calendar objects for expiration date and current date
        Calendar expiryDateCalendar = Calendar.getInstance();
        expiryDateCalendar.set(year, month, day); // Set expiration date
        Calendar currentDateCalendar = Calendar.getInstance(); // Current date
        // Compare expiration date with current date
        return expiryDateCalendar.before(currentDateCalendar);
    }



}
