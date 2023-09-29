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

import crm_project_22.entity.ProjectUser;
import crm_project_22.entity.User;
import crm_project_22.service.ProjectUserService;
import crm_project_22.service.UserService;

@WebFilter(filterName = "userFilter", urlPatterns = { "/users", "/create-user", "/update-user",
        "/user-detail", "/profile" })
public class UserFilter implements Filter {
    private UserService userService = new UserService();
    private ProjectUserService projectUserService = new ProjectUserService();

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

        String idParam = req.getParameter("id");

        switch (path) {
        case "/create-user":
            if (isAdmin) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(context);
            }
            break;

        case "/users":
            if (isAdminOrLeader) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(context);
            }
            break;

        case "/update-user":
        case "/profile":
            if (idParam != null && idParam.matches("\\d+")) {
                int userId = Integer.parseInt(idParam);
                User user = userService.getById(userId, false);

                if (user == null) {
                    res.sendRedirect(context);
                    return;
                }

                boolean isSameUser = userLoggedIn.getId() == user.getId();

                if (isAdmin || isSameUser) {
                    request.setAttribute("user", user);
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(context);
                }
                break;
            }

        case "/user-detail":
            if (!isAdminOrLeader) {
                res.sendRedirect(context);
                return;
            }

            String pidParam = req.getParameter("pid");

            if (idParam != null && idParam.matches("\\d+") && pidParam != null
                    && pidParam.matches("\\d+")) {
                int userId = Integer.parseInt(idParam);
                int projectId = Integer.parseInt(pidParam);

                ProjectUser projectUser = projectUserService.getOne(projectId, userId);

                if (projectUser == null) {
                    res.sendRedirect(context);
                    return;
                }

                boolean isLeaderOfProject = userLoggedIn.getId() == projectUser.getProject()
                        .getUser().getId();

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
