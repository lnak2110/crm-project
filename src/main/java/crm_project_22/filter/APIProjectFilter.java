package crm_project_22.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import crm_project_22.entity.Project;
import crm_project_22.entity.User;
import crm_project_22.service.ProjectService;
import crm_project_22.service.UserService;

@WebFilter(filterName = "apiProjectFilter", urlPatterns = { "/api/projects", "/api/projects/detail",
        "/api/projects/add-members", "/api/projects/user" })
public class APIProjectFilter implements Filter {
    private ProjectService projectService = new ProjectService();
    private UserService userService = new UserService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession httpSession = req.getSession();
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        boolean isAdmin = userLoggedIn != null
                && "ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName());
        boolean isAdminOrLeader = userLoggedIn != null
                && ("ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName())
                        || "LEADER".equalsIgnoreCase(userLoggedIn.getRole().getName()));

        if (!isAdminOrLeader) {
            return;
        }

        String path = req.getServletPath();
        String httpMethod = req.getMethod();

        String idParam = req.getParameter("id");

        switch (path) {
        case "/api/projects":
            if ("GET".equalsIgnoreCase(httpMethod) || "POST".equalsIgnoreCase(httpMethod)) {
                chain.doFilter(request, response);
            } else if ("PUT".equalsIgnoreCase(httpMethod)
                    || "DELETE".equalsIgnoreCase(httpMethod)) {
                if (idParam != null && idParam.matches("\\d+")) {
                    int projectId = Integer.parseInt(idParam);
                    Project project = projectService.getById(projectId);

                    if (project == null) {
                        return;
                    }

                    boolean isLeaderOfProject = userLoggedIn.getId() == project.getUser().getId();

                    if (isAdmin || isLeaderOfProject) {
                        chain.doFilter(request, response);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/projects/detail":
        case "/api/projects/add-members":
            if (idParam != null && idParam.matches("\\d+")) {
                int projectId = Integer.parseInt(idParam);
                Project project = projectService.getById(projectId);

                if (project == null) {
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == project.getUser().getId();

                if (isAdmin || isLeaderOfProject) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/projects/user":
            String uidParam = req.getParameter("uid");

            if (uidParam != null && uidParam.matches("\\d+")) {
                int userId = Integer.parseInt(uidParam);
                User user = userService.getById(userId, false);

                if (user == null) {
                    return;
                }

                boolean isSameUser = userLoggedIn.getId() == user.getId();

                if (isAdmin || isSameUser) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        default:
            break;
        }
    }
}
