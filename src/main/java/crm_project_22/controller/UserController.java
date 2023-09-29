package crm_project_22.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_22.entity.User;
import crm_project_22.service.ProjectService;
import crm_project_22.service.RoleService;
import crm_project_22.service.UserService;

@WebServlet(name = "userController", urlPatterns = { "/create-user", "/users", "/update-user",
        "/user-detail", "/profile" })
public class UserController extends HttpServlet {
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private ProjectService projectService = new ProjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.startsWith("/update-user")) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("user", userService.getById(id, true));
            req.setAttribute("roles", roleService.getAllRoles());

            req.getRequestDispatcher("jsp/update-user.jsp").forward(req, resp);
            return;
        }

        switch (path) {
        case "/create-user":
            req.setAttribute("roles", roleService.getAllRoles());

            req.getRequestDispatcher("jsp/create-user.jsp").forward(req, resp);
            break;

        case "/users":
            req.getRequestDispatcher("jsp/users.jsp").forward(req, resp);
            break;

        case "/user-detail":
            int pid = Integer.parseInt(req.getParameter("pid"));
            req.setAttribute("project", projectService.getById(pid));

            req.getRequestDispatcher("jsp/user-detail.jsp").forward(req, resp);
            break;

        case "/profile":
            User user = (User) req.getAttribute("user");

            boolean isAdminOrLeader = user != null
                    && ("ADMIN".equalsIgnoreCase(user.getRole().getName())
                            || "LEADER".equalsIgnoreCase(user.getRole().getName()));

            if (isAdminOrLeader) {
                req.getRequestDispatcher("jsp/profile-leader.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("jsp/profile.jsp").forward(req, resp);
            }

            break;

        default:
            break;
        }
    }
}
