<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Guestbook</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        h1 { color: #2c3e50; }
        form { margin-bottom: 20px; }
        .message { margin: 8px 0; padding: 10px; background: #f6f6f6; border-radius: 5px; }
        .name { font-weight: bold; color: #3498db; }
    </style>
</head>
<body>
<h1>Guestbook</h1>

<form method="post" action="guestbook-session">
    <label>Name session:</label><br>
    <input type="text" name="name" required><br><br>
    <label>Message session:</label><br>
    <textarea name="message" rows="3" cols="30" required></textarea><br><br>
    <button type="submit">Add</button>
</form>

<h2>Messages:</h2>
<c:forEach var="msg" items="${messages}">
    <div class="message">
        <span class="name">${msg.name}</span>: ${msg.content}
    </div>
</c:forEach>

</body>
</html>
