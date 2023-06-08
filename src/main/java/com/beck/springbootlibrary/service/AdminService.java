package com.beck.springbootlibrary.service;


import com.beck.springbootlibrary.entity.Book;
import com.beck.springbootlibrary.repo.BookRepository;
import com.beck.springbootlibrary.repo.CheckoutRepository;
import com.beck.springbootlibrary.repo.ReviewRepository;
import com.beck.springbootlibrary.requestmodels.AddBookRequest;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;

    private final ReviewRepository reviewRepository;

    @Autowired
    public AdminService(BookRepository bookRepository, CheckoutRepository checkoutRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.reviewRepository = reviewRepository;
    }


    public void postBook(AddBookRequest addBookRequest){
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());

        bookRepository.save(book);

    }

    public void increaseBookQuantity(Long bookId) throws Exception {

        Optional<Book> bookById = bookRepository.findById(bookId);

        if (!bookById.isPresent()){
            throw  new Exception("Book does not exist");
        }
        bookById.get().setCopiesAvailable(bookById.get().getCopiesAvailable()+1);
        bookById.get().setCopies(bookById.get().getCopies()+1);
        bookRepository.save(bookById.get());
    }

    public void decreaseBoookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if (!book.isPresent() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0){
            throw  new Exception("Book does not exist or quantity locked");
        }
        book.get().setCopies(book.get().getCopies() -1);
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());

    }

    public void deleteBook(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        if (!book.isPresent()){
            throw new Exception("Book does not exist");
        }
        bookRepository.deleteById(bookId);
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }

}
