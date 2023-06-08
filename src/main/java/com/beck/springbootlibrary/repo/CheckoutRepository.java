package com.beck.springbootlibrary.repo;


import com.beck.springbootlibrary.entity.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Checkout findByUserEmailAndBookId(String userEmail, Long bookId);

    List<Checkout> findAllByUserEmail(String userEmail); /* mening versiyam*/

//    List<Checkout> findBooksByUserEmail(String userEmail); video dagi

    @Modifying
    @Query("delete from Checkout where book_id in :book_id")
    void  deleteAllByBookId(@Param("book_id") Long bookId);

}
