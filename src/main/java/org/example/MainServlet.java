package org.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String currentPath = req.getParameter("path");
        if (currentPath == null || !currentPath.startsWith("C:")) {
            currentPath = System.getProperty("os.name").toLowerCase().startsWith("win") ? "C:" : "/opt/tomcat/";
        }
        File file = new File(currentPath);
        if (file.isFile()) {
            resp.setContentType("text/plain");
            resp.setHeader("Content-disposition", "attachment; filename=" + currentPath);
            try (InputStream in = req.getServletContext().getResourceAsStream(req.getParameter("path"));
                 OutputStream out = resp.getOutputStream()) {
                byte[] buffer = new byte[1048];
                int numBytesRead;
                while ((numBytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, numBytesRead);
                }
            }
        }
        showFiles(req, new File(currentPath).listFiles(), currentPath);
        req.setAttribute("currentPath", currentPath);
        req.getRequestDispatcher("FileManager.jsp").forward(req, resp);
    }

    private void showFiles(HttpServletRequest req, File[] files, String currentPath) {
        StringBuilder attrFolders = new StringBuilder();
        StringBuilder attrFiles = new StringBuilder();
        for (File file : files) {
            if (file.isDirectory()) {
                attrFolders.append("<li><a href=\"?path=").append(currentPath).append("/").append(file.getName())
                        .append("\">")
                        .append(file.getName())
                        .append("</a></li>");
            } else {
                attrFiles.append("<li><a href=\"?path=").append(currentPath).append("/").append(file.getName())
                        .append("\">")
                        .append(file.getName())
                        .append("</a></li>");
            }
        }
        req.setAttribute("folders", attrFolders);
        req.setAttribute("files", attrFiles);
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String previousPath = req.getContextPath();
//        String newStr = new StringBuilder(previousPath).reverse().toString();
//        int k = 0;
//        for (int i = 0; i < newStr.length(); i++) {
//            if (newStr.toCharArray()[i] == '\\') {
//                k = i;
//                break;
//            }
//        }
//        String returnStr = newStr.substring(k);
//        resp.sendRedirect(new StringBuilder(returnStr).reverse().toString());
//    }
}