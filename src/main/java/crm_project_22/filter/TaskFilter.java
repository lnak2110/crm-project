package crm_project_22.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import crm_project_22.entity.Task;
import crm_project_22.entity.User;
import crm_project_22.service.TaskService;

@WebFilter(filterName = "taskFilter", urlPatterns = { "/tasks", "/create-task", "/update-task" })
public class TaskFilter implements Filter {
    private TaskService taskService = new TaskService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();
        String context = req.getContextPath();

        HttpSession httpSession = req.getSession();
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        boolean isAdmin = userLoggedIn != null
                && "ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName());
        boolean isAdminOrLeader = userLoggedIn != null
                && ("ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName())
                        || "LEADER".equalsIgnoreCase(userLoggedIn.getRole().getName()));

        if (!isAdminOrLeader) {
            res.sendRedirect(context);
            return;
        }

        switch (path) {
        case "/create-task":
        case "/tasks":
            chain.doFilter(request, response);
            break;

        case "/update-task":
            String idParam = req.getParameter("id");

            if (idParam != null && idParam.matches("\\d+")) {
                int taskId = Integer.parseInt(idParam);
                Task task = taskService.getById(taskId);

                if (task == null) {
                    res.sendRedirect(context);
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == task.getProject().getUser()
                        .getId();

                if (isAdmin || isLeaderOfProject) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(context);
                }
            } else {
                res.sendRedirect(context);
            }
            break;

        default:
            break;
        }
    }
}
