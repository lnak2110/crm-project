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

import crm_project_22.entity.User;

@WebFilter(filterName = "apiRoleFilter", urlPatterns = "/api/roles")
public class APIRoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession httpSession = req.getSession();
        User userLoggedIn = (User) httpSession.getAttribute("userLoggedIn");

        boolean isAdmin = userLoggedIn != null
                && "ADMIN".equalsIgnoreCase(userLoggedIn.getRole().getName());

        if (!isAdmin) {
            return;
        }

        String path = req.getServletPath();
        String httpMethod = req.getMethod();

        String idParam = req.getParameter("id");

        switch (path) {
        case "/api/roles":
            if ("GET".equalsIgnoreCase(httpMethod)) {
                chain.doFilter(request, response);
            } else if ("PUT".equalsIgnoreCase(httpMethod)
                    || "DELETE".equalsIgnoreCase(httpMethod)) {
                if (idParam != null && idParam.matches("\\d+")) {
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
