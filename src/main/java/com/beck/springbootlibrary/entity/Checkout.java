package com.beck.springbootlibrary.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "checkout")
public class Checkout {

    public Checkout() {
    }

    public Checkout(String userEmail, String checkOutDate, String returnDate, Long bookId) {
        this.userEmail = userEmail;
        this.checkOutDate = checkOutDate;
        this.returnDate = returnDate;
        this.bookId = bookId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "checkout_date")
    private String checkOutDate;

    @Column(name = "return_date")
    private String returnDate;

    @Column(name = "book_id")
    private Long bookId;


}
