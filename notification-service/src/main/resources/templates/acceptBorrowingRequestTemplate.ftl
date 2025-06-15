<#-- Book Borrowing Confirmation Email Template -->

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accept Borrowing Request ID</title>
</head>
<body style="font-family: Arial, sans-serif; color: #000000;">

<p>Dear ${studentName},</p>
<br/>
<p>
    This is to confirm that your borrowing request has been successfully approved.
</p>

<p><strong>Borrowing Request Details:</strong></p>
<ul>
    <#list borrowedBooks as book>
        <li>
            <strong>${book.title}</strong>  - ${book.author.name} - ${book.shelf.location}<br>
        </li>
    </#list>
</ul>

<p>
    Please make sure to collect the book within 3 working days from the accept borrowing request date.
    If you fail to do so, your borrowing request will be automatically cancelled.
</p>
<p>
    If you have any questions or concerns, feel free to contact the library staff.
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
