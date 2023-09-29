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

@WebFilter(filterName = "apiStatusFilter", urlPatterns = { "/api/statuses/percents/in-project",
        "/api/statuses/percents/user-in-project", "/api/statuses/percents/user",
        "/api/statuses/percents/leader" })
public class APIStatusFilter implements Filter {
    private UserService userService = new UserService();
    private ProjectService projectService = new ProjectService();

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

        String path = req.getServletPath();

        String pidParam = req.getParameter("pid");
        String uidParam = req.getParameter("uid");

        switch (path) {
        case "/api/statuses":
            chain.doFilter(request, response);
            break;

        case "/api/statuses/percents/in-project":
            if (pidParam != null && pidParam.matches("\\d+")) {
                int projectId = Integer.parseInt(pidParam);
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

        case "/api/statuses/percents/user-in-project":
            if (pidParam != null && pidParam.matches("\\d+") && uidParam != null
                    && uidParam.matches("\\d+")) {
                int projectId = Integer.parseInt(pidParam);
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

        case "/api/statuses/percents/user":
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

        case "/api/statuses/percents/leader":
            if (!isAdminOrLeader) {
                return;
            }
            
            if (uidParam != null && uidParam.matches("\\d+")) {
                chain.doFilter(request, response);
            } else {
                return;
            }
            break;

        default:
            break;
        }
    }
}
