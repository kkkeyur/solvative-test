-- =========================
-- BOOKS
-- =========================
INSERT INTO books (isbn, title, author, genre, total_copies, available_copies, version)
VALUES
    ('978-1234567890', 'Clean Code', 'Robert C. Martin', 'Programming', 5, 3, 0),
    ('978-0987654321', 'Effective Java', 'Joshua Bloch', 'Programming', 3, 1, 0),
    ('978-1122334455', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 'Architecture', 4, 4, 0);

-- =========================
-- MEMBERS
-- =========================
INSERT INTO members (email, name, membership_status, joined_at)
VALUES
    ('member1@bookvault.com', 'Alice Johnson', 'ACTIVE', CURRENT_TIMESTAMP),
    ('member2@bookvault.com', 'Bob Smith', 'ACTIVE', CURRENT_TIMESTAMP),
    ('member3@bookvault.com', 'Charlie Brown', 'SUSPENDED', CURRENT_TIMESTAMP);

-- =========================
-- USERS
-- =========================
INSERT INTO auth_users (username, password, role, member_id)
VALUES
    ('librarian1', 'password123', 'LIBRARIAN', NULL),
    ('member1', 'password123', 'MEMBER', 1),
    ('member2', 'password123', 'MEMBER', 2);

-- =========================
-- LOANS
-- =========================

-- ACTIVE
INSERT INTO loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '14 days', NULL, 'ACTIVE'),
    (2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '14 days', NULL, 'ACTIVE');

-- RETURNED
INSERT INTO loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (
        1,
        2,
        CURRENT_TIMESTAMP - INTERVAL '20 days',
        CURRENT_TIMESTAMP - INTERVAL '6 days',
        CURRENT_TIMESTAMP - INTERVAL '5 days',
        'RETURNED'
    );

-- OVERDUE
INSERT INTO loans (book_id, member_id, borrowed_at, due_date, returned_at, status)
VALUES
    (
        3,
        1,
        CURRENT_TIMESTAMP - INTERVAL '20 days',
        CURRENT_TIMESTAMP - INTERVAL '5 days',
        NULL,
        'ACTIVE'
    );