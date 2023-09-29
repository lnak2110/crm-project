package crm_project_22.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_22.service.RoleService;

@WebServlet(name = "roleController", urlPatterns = { "/create-role", "/roles", "/update-role" })
public class RoleController extends HttpServlet {
    private RoleService roleService = new RoleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.startsWith("/update-role")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("role", roleService.getById(id));

            req.getRequestDispatcher("jsp/update-role.jsp").forward(req, resp);
            return;
        }

        switch (path) {
        case "/create-role":
            req.getRequestDispatcher("jsp/create-role.jsp").forward(req, resp);
            break;

        case "/roles":
            req.getRequestDispatcher("jsp/roles.jsp").forward(req, resp);
            break;

        default:
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
        case "/create-role":
            String name = req.getParameter("name");
            String description = req.getParameter("description");

            req.setAttribute("isSuccess", roleService.checkCreateRole(name, description));

            req.getRequestDispatcher("jsp/create-role.jsp").forward(req, resp);
            break;

        default:
            break;
        }
    }
}
