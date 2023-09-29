package crm_project_22.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_22.service.ProjectService;
import crm_project_22.service.StatusService;

@WebServlet(name = "projectController", urlPatterns = { "/create-project", "/projects",
        "/update-project", "/project-detail" })
public class ProjectController extends HttpServlet {
    private ProjectService projectService = new ProjectService();
    private StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.startsWith("/update-project")) {
            int id = Integer.parseInt(req.getParameter("id"));

            req.setAttribute("project", projectService.getById(id));
            req.setAttribute("statuses", statusService.getAll());

            req.getRequestDispatcher("jsp/update-project.jsp").forward(req, resp);
            return;
        }

        if (path.startsWith("/project-detail")) {
            int id = Integer.parseInt(req.getParameter("id"));

            req.setAttribute("project", projectService.getById(id));

            req.getRequestDispatcher("jsp/project-detail.jsp").forward(req, resp);
            return;
        }

        switch (path) {
        case "/create-project":
            req.setAttribute("statuses", statusService.getAll());

            req.getRequestDispatcher("jsp/create-project.jsp").forward(req, resp);
            break;

        case "/projects":
            req.getRequestDispatcher("jsp/projects.jsp").forward(req, resp);
            break;

        default:
            break;
        }
    }
}