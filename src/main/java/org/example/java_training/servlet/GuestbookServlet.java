package org.example.java_training.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.java_training.domain.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/guestbook")
public class GuestbookServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    private List<Message> getMessages() {
        List<Message> messages = (List<Message>) getServletContext().getAttribute("messages");
        if (messages == null) {
            messages = new ArrayList<>();
            getServletContext().setAttribute("messages", messages);
        }
        return messages;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("messages", getMessages());
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String content = req.getParameter("message");

        if (name != null && !name.isEmpty() && content != null && !content.isEmpty()) {
            List<Message> messages = getMessages();
            messages.add(new Message(name, content));
            System.out.println("✅ Message added: " + name + " -> " + content);
        } else {
            System.out.println("⚠️ Missing name or message, nothing added");
        }

        resp.sendRedirect("guestbook");
    }
}
