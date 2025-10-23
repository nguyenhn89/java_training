package org.example.java_training.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.java_training.domain.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/guestbook-session")
public class GuestbookSessionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        List<Message> messages = (List<Message>) session.getAttribute("messages");

        if (messages == null) {
            messages = new ArrayList<>();
            session.setAttribute("messages", messages); // khởi tạo mới nếu chưa có
        }

        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/WEB-INF/index_test.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String content = req.getParameter("message");

        HttpSession session = req.getSession();
        List<Message> messages = (List<Message>) session.getAttribute("messages");

        if (messages == null) {
            messages = new ArrayList<>();
            session.setAttribute("messages", messages);
        }

        if (name != null && !name.isEmpty() && content != null && !content.isEmpty()) {
            messages.add(new Message(name, content));
        }

        session.setAttribute("messages", messages);

        req.setAttribute("messages", messages);
        req.getRequestDispatcher("/WEB-INF/index_test.jsp").forward(req, resp);
    }
}
