package com.example.hellowebapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/factorial")
public class MathServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String numStr = req.getParameter("number");
        try {
            int n = Integer.parseInt(numStr);
            long fact = factorial(n);
            out.print("{\"number\": " + n + ", \"factorial\": " + fact + "}");
        } catch (Exception e) {
            out.print("{\"error\": \"Invalid or missing number parameter\"}");
        }
        out.flush();
    }

    private long factorial(int n) {
        if (n < 0) return -1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
