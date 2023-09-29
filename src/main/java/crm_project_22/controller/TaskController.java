package crm_project_22.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import crm_project_22.service.ProjectService;
import crm_project_22.service.StatusService;
import crm_project_22.service.TaskService;

@WebServlet(name = "taskController", urlPatterns = { "/create-task", "/tasks", "/update-task" })
public class TaskController extends HttpServlet {
    private TaskService taskService = new TaskService();
    private ProjectService projectService = new ProjectService();
    private StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();

        if (path.startsWith("/update-task")) {
            int id = Integer.parseInt(req.getParameter("id"));

            req.setAttribute("task", taskService.getById(id));
            req.setAttribute("statuses", statusService.getAll());

            req.getRequestDispatcher("jsp/update-task.jsp").forward(req, resp);
            return;
        }

        switch (path) {
        case "/create-task":
            req.setAttribute("statuses", statusService.getAll());

            req.getRequestDispatcher("jsp/create-task.jsp").forward(req, resp);
            break;

        case "/tasks":
            req.getRequestDispatcher("jsp/tasks.jsp").forward(req, resp);
            break;

        default:
            break;
        }
    }
}
