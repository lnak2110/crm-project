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

import crm_project_22.entity.User;

@WebFilter(filterName = "roleFilter", urlPatterns = { "/roles", "/create-role", "/update-role" })
public class RoleFilter implements Filter {
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

        if (!isAdmin) {
            res.sendRedirect(context);
            return;
        }

        switch (path) {
        case "/create-role":
        case "/roles":
            chain.doFilter(request, response);
            break;

        case "/update-role":
            String id = req.getParameter("id");
            if (id != null && id.matches("\\d+")) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(context);
            }
            break;

        default:
            break;
        }
    }
}
