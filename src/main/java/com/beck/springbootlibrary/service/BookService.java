package com.beck.springbootlibrary.service;


import com.beck.springbootlibrary.entity.Book;
import com.beck.springbootlibrary.entity.Checkout;
import com.beck.springbootlibrary.entity.History;
import com.beck.springbootlibrary.repo.BookRepository;
import com.beck.springbootlibrary.repo.CheckoutRepository;
import com.beck.springbootlibrary.repo.HistoryRepository;
import com.beck.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    private final HistoryRepository historyRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }




    public Book checkoutBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (!book.isPresent() || validateCheckout != null || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book does not exist or already checked out by user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(31).toString(),
                book.get().getId()
        );
        checkoutRepository.save(checkout);
        return book.get();
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        if (validateCheckout != null){
            return true;
        }
        return false;
    }
    public int currentLoansCount(String userEmail){
        return checkoutRepository.findAllByUserEmail(userEmail).size();
    }
    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws ParseException {
         List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();


        //Check out list that user checked out
        List<Checkout> checkoutList = checkoutRepository.findAllByUserEmail(userEmail);

        //Book ids were checked out by the user, all Ids are unique because User can only check out book only once
        List<Long> bookIds = new ArrayList<>();
        for (Checkout checkout: checkoutList){
            bookIds.add(checkout.getBookId());
        }

        //Books were checked out by user. All books are unique ser can only check out a book only once
        List<Book> booksCheckoutByUser = bookRepository.findBooksByBookIds(bookIds);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

          for (Book book: booksCheckoutByUser){
                Optional<Checkout> checkout = checkoutList.stream().
                        filter(x -> x.getBookId() == book.getId()).findFirst();

                if (checkout.isPresent()){

                    Date dt1 =  sdf.parse(checkout.get().getReturnDate());
                    Date dt2 = sdf.parse(LocalDate.now().toString());

                    TimeUnit timeUnit = TimeUnit.DAYS;

                    long differ_in_time = timeUnit.convert(dt1.getTime() - dt2.getTime(),TimeUnit.MILLISECONDS);

                    shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) differ_in_time));
                }
          }

        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (!book.isPresent() || validateCheckout == null){
            throw new Exception("Book does not exist or not checked out by user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());

        History history  = new History(
                userEmail,
                validateCheckout.getCheckOutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );
        historyRepository.save(history);
    }

    public void renewLoan(String userEmail,Long bookId) throws Exception {

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (validateCheckout == null){
            throw new Exception("Book does not exist or not checked out by user");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date dt1 = sdf.parse(validateCheckout.getReturnDate());
        Date dt2 = sdf.parse(LocalDate.now().toString());

        if (dt1.compareTo(dt2) >= 0){
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }
    }

}
