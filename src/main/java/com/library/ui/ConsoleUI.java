package com.library.ui;

import com.library.exception.BookNotAvailableException;
import com.library.exception.InvalidLoginException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Member;
import com.library.model.Notification;
import com.library.model.Staff;
import com.library.model.Transaction;
import com.library.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final BookService bookService = new BookService();
    private final MemberService memberService = new MemberService();
    private final StaffService staffService = new StaffService();
    private final TransactionService transactionService = new TransactionService();
    private final FineService fineService = new FineService();
    private final NotificationService notificationService = new NotificationService();

    public void start() {
        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Staff/Admin Login");
            System.out.println("2. Member Menu");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> handleStaffLogin();
                case "2" -> handleMemberMenu();
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleStaffLogin() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            Staff staff = staffService.login(email, password);
            System.out.println("Welcome, " + staff.getName() + " [" + staff.getRole() + "]");
            if ("ADMIN".equalsIgnoreCase(staff.getRole())) {
                adminMenu();
            } else {
                staffMenu();
            }
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Search Books");
            System.out.println("5. List Books");
            System.out.println("6. Register Member");
            System.out.println("7. View Active Members");
            System.out.println("8. View Overdue Books");
            System.out.println("9. View Fines");
            System.out.println("10. View All Notifications");
            System.out.println("11. Back");
            System.out.print("Choose option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> addBook();
                case "2" -> updateBook();
                case "3" -> deleteBook();
                case "4" -> searchBooks();
                case "5" -> listBooks();
                case "6" -> registerMember();
                case "7" -> listActiveMembers();
                case "8" -> printOverdueBooks();
                case "9" -> listFines();
                case "10" -> viewAllNotifications();
                case "11" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void staffMenu() {
        while (true) {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("1. Issue Book");
            System.out.println("2. Return Book");
            System.out.println("3. List Books");
            System.out.println("4. Search Books");
            System.out.println("5. List Members");
            System.out.println("6. View All Notifications");
            System.out.println("7. Back");
            System.out.print("Choose option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> issueBook();
                case "2" -> returnBook();
                case "3" -> listBooks();
                case "4" -> searchBooks();
                case "5" -> listMembers();
                case "6" -> viewAllNotifications();
                case "7" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleMemberMenu() {
        System.out.print("Enter member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        try {
            Member member = memberService.getMemberById(memberId);
            System.out.println("Welcome member: " + member.getName());
            while (true) {
                System.out.println("\n--- Member Menu ---");
                System.out.println("1. View My Notifications");
                System.out.println("2. View Borrowing History");
                System.out.println("3. Back");
                System.out.print("Choose option: ");
                String option = scanner.nextLine();
                switch (option) {
                    case "1" -> viewMyNotifications(memberId);
                    case "2" -> showBorrowingHistory(memberId);
                    case "3" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (MemberNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addBook() {
        Book book = new Book();
        System.out.print("Title: "); book.setTitle(scanner.nextLine());
        System.out.print("Author: "); book.setAuthor(scanner.nextLine());
        System.out.print("ISBN: "); book.setIsbn(scanner.nextLine());
        System.out.print("Category: "); book.setCategory(scanner.nextLine());
        System.out.print("Total copies: "); book.setTotalCopies(Integer.parseInt(scanner.nextLine()));
        book.setAvailableCopies(book.getTotalCopies());
        bookService.addBook(book);
        System.out.println("Book added successfully.");
    }

    private void updateBook() {
        System.out.print("Book ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Book book = bookService.findById(id).orElse(null);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.print("New title: "); book.setTitle(scanner.nextLine());
        System.out.print("New author: "); book.setAuthor(scanner.nextLine());
        System.out.print("New ISBN: "); book.setIsbn(scanner.nextLine());
        System.out.print("New category: "); book.setCategory(scanner.nextLine());
        System.out.print("New total copies: "); book.setTotalCopies(Integer.parseInt(scanner.nextLine()));
        System.out.print("New available copies: "); book.setAvailableCopies(Integer.parseInt(scanner.nextLine()));
        bookService.updateBook(book);
        System.out.println("Book updated.");
    }

    private void deleteBook() {
        System.out.print("Book ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        bookService.deleteBook(id);
        System.out.println("Book deleted.");
    }

    private void searchBooks() {
        System.out.print("Search keyword: ");
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooks(keyword);
        books.forEach(System.out::println);
    }

    private void listBooks() {
        bookService.listAllBooks().forEach(System.out::println);
    }

    private void registerMember() {
        Member member = new Member();
        System.out.print("Name: "); member.setName(scanner.nextLine());
        System.out.print("Email: "); member.setEmail(scanner.nextLine());
        System.out.print("Phone: "); member.setPhone(scanner.nextLine());
        System.out.print("Address: "); member.setAddress(scanner.nextLine());
        member.setStatus("ACTIVE");
        memberService.registerMember(member);
        System.out.println("Member registered.");
    }

    private void listMembers() {
        memberService.listActiveMembers().forEach(System.out::println);
    }

    private void listActiveMembers() {
        memberService.listActiveMembers().forEach(System.out::println);
    }

    private void issueBook() {
        System.out.print("Book ID: "); int bookId = Integer.parseInt(scanner.nextLine());
        System.out.print("Member ID: "); int memberId = Integer.parseInt(scanner.nextLine());
        try {
            transactionService.issueBook(bookId, memberId);
            notificationService.createNotification(memberId, "Book issued successfully.", "ISSUE");
            System.out.println("Book issued.");
        } catch (BookNotAvailableException | MemberNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void returnBook() {
        System.out.print("Transaction ID: "); int transactionId = Integer.parseInt(scanner.nextLine());
        transactionService.returnBook(transactionId);
        System.out.println("Book returned and fine evaluated.");
    }

    private void printOverdueBooks() {
        transactionService.getOverdueTransactions().forEach(t -> System.out.println("Transaction ID: " + t.getTransactionId() + ", Member ID: " + t.getMemberId() + ", Due: " + t.getDueDate()));
    }

    private void listFines() {
        List<Fine> fines = fineService.getAllFines();
        for (Fine fine : fines) {
            System.out.println("Fine ID: " + fine.getFineId() + " | Member ID: " + fine.getMemberId() + " | Amount: " + fine.getAmount() + " | Paid: " + fine.isPaidStatus());
        }
    }

    private void viewAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        notifications.forEach(n -> System.out.println("[" + n.getType() + "] Member " + n.getMemberId() + ": " + n.getMessage() + " | Read=" + n.isRead()));
        System.out.print("Mark which notification as read? (0 to skip): ");
        int id = Integer.parseInt(scanner.nextLine());
        if (id > 0) {
            notificationService.markAsRead(id);
            System.out.println("Notification marked as read.");
        }
    }

    private void viewMyNotifications(int memberId) {
        List<Notification> notifications = notificationService.getNotificationsForMember(memberId);
        notifications.forEach(n -> System.out.println("[" + n.getType() + "] " + n.getMessage() + " | Read=" + n.isRead()));
        System.out.print("Mark which notification as read? (0 to skip): ");
        int id = Integer.parseInt(scanner.nextLine());
        if (id > 0) {
            notificationService.markAsRead(id);
            System.out.println("Notification marked as read.");
        }
    }

    private void showBorrowingHistory(int memberId) {
        List<Transaction> history = memberService.getBorrowingHistory(memberId);
        for (Transaction transaction : history) {
            System.out.println("Transaction ID: " + transaction.getTransactionId() + " | Book ID: " + transaction.getBookId() + " | Due: " + transaction.getDueDate() + " | Status: " + transaction.getStatus());
        }
    }
}
