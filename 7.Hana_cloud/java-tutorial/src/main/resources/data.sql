
-- Create the books table if it doesn't exist
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL
    );

-- Insert initial data into the books table
INSERT INTO books (book_name, author, quantity) VALUES
('Book 1', 'Author 1', 14),
('Book 2', 'Author 2', 3),
('Book 3', 'Author 3', 1);