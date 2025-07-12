<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Return Reminder - 3 Days Left</title>
</head>
<body style="font-family: Arial, sans-serif; color: #000000;">

<p>Dear ${studentName},</p>
<br/>
<p>
    This is a friendly reminder that you have <strong>3 days left</strong> to return the borrowed book(s) to the library.
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

<p>
    Please make sure to return the books by the due date to avoid any late penalties. If you need assistance or have any questions, feel free to reach out to us.
</p>

<p>Thank you for your attention.</p>

<p>Best regards,</p>
<br>
<p>
    <strong>Library Management System</strong><br>
    ${librarianName}<br>
    Mail: <a href="mailto:${librarianEmail}">${librarianEmail}</a>
</p>

</body>
</html>
