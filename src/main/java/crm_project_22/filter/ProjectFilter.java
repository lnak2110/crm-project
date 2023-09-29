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

import crm_project_22.entity.Project;
import crm_project_22.entity.User;
import crm_project_22.service.ProjectService;

@WebFilter(filterName = "projectFilter", urlPatterns = { "/projects", "/create-project",
        "/update-project", "/project-detail" })
public class ProjectFilter implements Filter {
    private ProjectService projectService = new ProjectService();

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
        case "/create-project":
        case "/projects":
            chain.doFilter(request, response);
            break;

        case "/update-project":
        case "/project-detail":
            String idParam = req.getParameter("id");

            if (idParam != null && idParam.matches("\\d+")) {
                int projectId = Integer.parseInt(idParam);
                Project project = projectService.getById(projectId);

                if (project == null) {
                    res.sendRedirect(context);
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == project.getUser().getId();

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
