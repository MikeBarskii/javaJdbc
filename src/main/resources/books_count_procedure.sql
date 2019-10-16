DELIMITER //

CREATE PROCEDURE booksCount (OUT cnt INT)
    BEGIN
        SELECT COUNT(*) INTO cnt FROM Books;
    END //