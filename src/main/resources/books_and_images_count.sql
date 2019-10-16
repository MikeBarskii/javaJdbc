DELIMITER //

CREATE PROCEDURE tablesCount()
    BEGIN
        SELECT COUNT(*) FROM Books;
        SELECT COUNT(*) FROM Images;
    END //