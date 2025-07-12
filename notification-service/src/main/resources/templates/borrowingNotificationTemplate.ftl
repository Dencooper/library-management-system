<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Borrowing Confirmation</title>
</head>
<body style="font-family: Arial, sans-serif; color: #000000;">

<p>Dear ${studentName},</p>
<br/>
<p>
    Your borrowing request has been successfully processed. Below are the details of your borrowing transaction.
</p>

<p><strong>📚 Borrowed Book(s):</strong></p>
<ul>
    <#list borrowedBooks as book>
        <li>
            <strong>${book.bookTitle}</strong> - Code: ${book.code} <br>
            Condition: ${book.bookItemCondition} - Price: $${book.price}
        </li>
    </#list>
</ul>

<p><strong>📅 Borrowing Details:</strong></p>
<ul>
    <li><strong>Borrowing Date:</strong> ${borrowedAt}</li>
    <li><strong>Due Date:</strong> ${dueDate}</li>
</ul>

<p><strong>👤 Student Information:</strong></p>
<ul>
    <li><strong>Name:</strong> ${studentName}</li>
    <li><strong>Email:</strong> <a href="mailto:${studentEmail}">${studentEmail}</a></li>
    <li><strong>Phone:</strong> ${studentPhone}</li>
</ul>

<p><strong>👩‍💼 Approved By:</strong></p>
<ul>
    <li><strong>Name:</strong> ${librarianName}</li>
    <li><strong>Email:</strong> <a href="mailto:${librarianEmail}">${librarianEmail}</a></li>
    <li><strong>Phone:</strong> ${librarianPhone}</li>
</ul>

<p>
    Please ensure that you return the books by the due date to avoid any penalties. If you have any questions or concerns, feel free to contact the library staff.
</p>

<p>Thank you for using our library services.</p>

<p>Best regards,</p>
<br>
<p>
    <strong>Library Management System</strong><br>
    ${librarianName}<br>
    Mail: <a href="mailto:${librarianEmail}">${librarianEmail}</a>
</p>

</body>
</html>
