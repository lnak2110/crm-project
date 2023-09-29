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
import crm_project_22.entity.ProjectUser;
import crm_project_22.entity.User;
import crm_project_22.service.ProjectService;
import crm_project_22.service.ProjectUserService;
import crm_project_22.service.UserService;

@WebFilter(filterName = "apiUserFilter", urlPatterns = { "/api/users", "/api/users/outside",
        "/api/users/in-project", "/api/users/detail", "/api/users/profile" })
public class APIUserFilter implements Filter {
    private UserService userService = new UserService();
    private ProjectService projectService = new ProjectService();
    private ProjectUserService projectUserService = new ProjectUserService();

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
        String httpMethod = req.getMethod();

        String idParam = req.getParameter("id");
        String pidParam = req.getParameter("pid");

        switch (path) {
        case "/api/users":
            if ("PUT".equalsIgnoreCase(httpMethod)) {
                if (idParam != null && idParam.matches("\\d+")) {
                    int userId = Integer.parseInt(idParam);
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
            }

            if (!isAdminOrLeader) {
                return;
            }

            if ("GET".equalsIgnoreCase(httpMethod)) {
                chain.doFilter(request, response);
            } else if ("POST".equalsIgnoreCase(httpMethod)) {
                if (isAdmin) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else if ("DELETE".equalsIgnoreCase(httpMethod)) {
                if (idParam != null && idParam.matches("\\d+")) {
                    if (isAdmin) {
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

        case "/api/users/outside":
        case "/api/users/in-project":
            if (!isAdminOrLeader) {
                return;
            }

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

        case "/api/users/detail":
            if (!isAdminOrLeader) {
                return;
            }

            if (idParam != null && idParam.matches("\\d+") && pidParam != null
                    && pidParam.matches("\\d+")) {
                int userId = Integer.parseInt(idParam);
                int projectId = Integer.parseInt(pidParam);

                ProjectUser projectUser = projectUserService.getOne(projectId, userId);

                if (projectUser == null) {
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == projectUser.getProject()
                        .getUser().getId();

                if (isAdmin || isLeaderOfProject) {
                    chain.doFilter(request, response);
                } else {
                    return;
                }
            } else {
                return;
            }
            break;

        case "/api/users/profile":
            if (idParam != null && idParam.matches("\\d+")) {
                int userId = Integer.parseInt(idParam);
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
