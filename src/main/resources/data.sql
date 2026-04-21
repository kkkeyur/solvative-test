-- =========================
-- BOOKS
-- =========================
INSERT INTO Books (isbn, title, author, genre, total_copies, available_copies, version)
VALUES
    ('978-1234567890', 'Clean Code', 'Robert C. Martin', 'Programming', 5, 3, 0),
    ('978-0987654321', 'Effective Java', 'Joshua Bloch', 'Programming', 3, 1, 0),
    ('978-1122334455', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 'Architecture', 4, 4, 0),
    ('978-6677889900', 'Refactoring', 'Martin Fowler', 'Programming', 6, 5, 0),
    ('978-5566778899', 'The Pragmatic Programmer', 'Andrew Hunt', 'Programming', 4, 2, 0);

-- =========================
-- MEMBERS
-- =========================
INSERT INTO Members (email, name, membership_status, joined_at)
VALUES
    ('member1@bookvault.com', 'Alice Johnson', 'ACTIVE', CURRENT_TIMESTAMP),
    ('member2@bookvault.com', 'Bob Smith', 'ACTIVE', CURRENT_TIMESTAMP),
    ('member3@bookvault.com', 'Charlie Brown', 'SUSPENDED', CURRENT_TIMESTAMP),
    ('member4@bookvault.com', 'David Wilson', 'ACTIVE', CURRENT_TIMESTAMP),
    ('member5@bookvault.com', 'Eve Adams', 'ACTIVE', CURRENT_TIMESTAMP);

-- =========================
-- AUTH USERS (ROLES)
-- =========================
-- Password: password123 (BCrypt - replace if needed)

INSERT INTO Auth_users (username, password, role, member_id)
VALUES
    ('librarian1', 'password123', 'LIBRARIAN', NULL),
    ('librarian2', 'password123', 'LIBRARIAN', NULL),

    ('member1', 'password123', 'MEMBER', 1),
    ('member2', 'password123', 'MEMBER', 2),
    ('member3', 'password123', 'MEMBER', 3),
    ('member4', 'password123', 'MEMBER', 4);

-- =========================
-- LOANS
-- =========================
-- ACTIVE LOANS
INSERT INTO Loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (1, 1, CURRENT_TIMESTAMP, DATEADD('DAY', 14, CURRENT_TIMESTAMP), NULL, 'ACTIVE'),
    (2, 2, CURRENT_TIMESTAMP, DATEADD('DAY', 14, CURRENT_TIMESTAMP), NULL, 'ACTIVE');

-- RETURNED LOAN
INSERT INTO Loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (1, 2,
     DATEADD('DAY', -20, CURRENT_TIMESTAMP),
     DATEADD('DAY', -6, CURRENT_TIMESTAMP),
     DATEADD('DAY', -5, CURRENT_TIMESTAMP),
     'RETURNED');

-- OVERDUE LOAN (still ACTIVE but past due)
INSERT INTO Loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (4, 4,
     DATEADD('DAY', -20, CURRENT_TIMESTAMP),
     DATEADD('DAY', -5, CURRENT_TIMESTAMP),
     NULL,
     'ACTIVE');